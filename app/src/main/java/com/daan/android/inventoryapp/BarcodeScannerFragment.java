package com.daan.android.inventoryapp;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.BiConsumer;
import java8.util.function.Consumer;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private BiConsumer<String, BarcodeFormat> scannerListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scannerView = new ZXingScannerView(getContext());
        return scannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        RxPermissions rxPermissions = new RxPermissions(this);
        disposables.add(rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        scannerView.startCamera();
                    } else {
                        Toast.makeText(getContext(), R.string.need_camera_permissions, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.dispose();
    }

    public void setScannerListener(BiConsumer<String, BarcodeFormat> scannerListener) {
        this.scannerListener = scannerListener;
    }

    @Override
    public void handleResult(Result result) {
        if (this.scannerListener != null) {
            this.scannerListener.accept(result.getText(), result.getBarcodeFormat());
        }
    }
}
