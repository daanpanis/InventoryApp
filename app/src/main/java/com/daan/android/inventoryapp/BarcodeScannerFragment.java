package com.daan.android.inventoryapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.zbar.camera.CameraPreview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarcodeScannerFragment extends Fragment {

    @BindView(R.id.capture_preview)
    CameraPreview cameraPreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        cameraPreview.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        cameraPreview.stop();
    }
}
