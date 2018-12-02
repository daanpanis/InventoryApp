package com.daan.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daan.android.inventoryapp.adapters.ItemAdapter;
import com.daan.android.inventoryapp.models.Item;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java8.util.stream.IntStreams;

import static java8.util.stream.StreamSupport.stream;

public class ItemListFragment extends Fragment {

    private Disposable disposable;
    private final List<ItemAdapter.ItemHolder> items = new ArrayList<>();
    private ItemAdapter adapter;

    private Observable<DocumentChange> itemObservable;

    @BindView(R.id.items_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter((adapter = new ItemAdapter(items)));
        recyclerView.setHasFixedSize(true);

        if (itemObservable != null && disposable == null) {
            subscribe();
        }
    }

    public void setItemsObservable(Observable<Collection<DocumentChange>> itemsObservable) {
        setItemObservable(itemsObservable.flatMap(Observable::fromIterable));
    }

    public void setItemObservable(Observable<DocumentChange> itemObservable) {
        this.itemObservable = itemObservable;
        if (recyclerView != null) {
            subscribe();
        }
    }

    private void subscribe() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = itemObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(change -> {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        items.add(new ItemAdapter.ItemHolder(change.getDocument().getId(), change.getDocument().toObject(Item.class)));
                        adapter.notifyItemInserted(items.size() - 1);
                    } else if (change.getType() == DocumentChange.Type.MODIFIED) {
                        int indexOf = removeItem(change.getDocument().getId());
                        if (indexOf >= 0) {
                            items.add(indexOf, new ItemAdapter.ItemHolder(change.getDocument().getId(), change.getDocument().toObject(Item.class)));
                            adapter.notifyItemChanged(indexOf);
                        }
                    } else {
                        adapter.notifyItemRemoved(removeItem(change.getDocument().getId()));
                    }
                });
    }

    private int removeItem(String itemId) {
        int indexOf = IntStreams.range(0, items.size())
                .filter(i -> itemId.equals(items.get(i).id))
                .findFirst().orElse(-1);
        if (indexOf >= 0) {
            items.remove(indexOf);
        }
        return indexOf;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @OnClick(R.id.btn_create_item)
    void createItem() {
        startActivity(new Intent(getActivity(), CreateItemActivity.class));
    }
}
