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

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yanzhenjie.zbar.camera.CameraPreview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class BarcodeScannerFragment extends Fragment {

    @BindView(R.id.capture_preview)
    CameraPreview cameraPreview;

    private RxPermissions rxPermissions;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.d("BarcodeScannerFragment", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("BarcodeScannerFragment", "onStart");
        rxPermissions = new RxPermissions(this);
        disposables.add(rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        if (cameraPreview.start()) {
                            cameraPreview.setScanCallback(content -> {
                                Log.d("BarcodeScannerFragment", "Content: " + content);
                            });
                        }
                    } else {
                        // Oups permission denied
                    }
                }));
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.dispose();
        cameraPreview.stop();
    }
}
