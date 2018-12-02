package com.daan.android.inventoryapp.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daan.android.inventoryapp.R;
import com.daan.android.inventoryapp.models.Item;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.BiConsumer;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<ItemHolder> items;
    private BiConsumer<String, Item> onClickListener;


    public ItemAdapter(List<ItemHolder> items) {
        this.items = items;
    }

    public void setOnClickListener(BiConsumer<String, Item> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.bind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemHolder {

        public final String id;
        public final Item item;

        public ItemHolder(String id, Item item) {
            this.id = id;
            this.item = item;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private CompositeDisposable disposables;

        @BindView(R.id.tv_item_name)
        TextView itemName;
        @BindView(R.id.iv_item_picture)
        ImageView itemPicture;
        @BindView(R.id.iv_item_barcode)
        ImageView itemBarcode;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    ItemHolder holder = items.get(getAdapterPosition());
                    onClickListener.accept(holder.id, holder.item);
                }
            });
        }

        public void bind(ItemHolder holder) {
            this.disposables = new CompositeDisposable();
            itemName.setText(holder.item.getName());
            if (holder.item.getImageUrls().size() > 0) {
                Picasso.get().load(Uri.parse(holder.item.getImageUrls().get(0))).placeholder(R.drawable.placeholder_16_9).into(itemPicture);
            }
            BarcodeFormat format = getFormat(holder.item.getBarcodeFormat());
            if (holder.item.getBarcode() != null && format != null) {
                disposables.add(Observable.fromCallable(() -> {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    BitMatrix bitMatrix = multiFormatWriter.encode(holder.item.getBarcode(), BarcodeFormat.CODABAR, 1000, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    return barcodeEncoder.createBitmap(bitMatrix);
                }).observeOn(AndroidSchedulers.mainThread())
                        .doFinally(disposables::dispose)
                        .subscribe(itemBarcode::setImageBitmap));
            }
        }

        private BarcodeFormat getFormat(String format) {
            if (format == null) {
                return null;
            }
            for (BarcodeFormat barcodeFormat : BarcodeFormat.values()) {
                if (barcodeFormat.name().equals(format)) {
                    return barcodeFormat;
                }
            }
            return null;
        }
    }
}
