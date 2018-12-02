package com.daan.android.inventoryapp.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daan.android.inventoryapp.InventoryApplication;
import com.daan.android.inventoryapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavingFragment extends Fragment {


    @BindView(R.id.saving_progress)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        progressBar.setIndeterminate(true);
        InventoryApplication.getGlobalModel().isSaving().observe(this, saving ->
                view.setVisibility(saving != null && saving ? View.VISIBLE : View.GONE)
        );
    }
}
