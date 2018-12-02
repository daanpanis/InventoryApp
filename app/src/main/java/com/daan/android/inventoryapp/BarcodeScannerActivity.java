package com.daan.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static com.daan.android.inventoryapp.utils.Constants.BARCODE_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.BARCODE_FORMAT_EXTRA;

public class BarcodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        BarcodeScannerFragment scanner = (BarcodeScannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_barcode_scanner);
        if (scanner != null) {
            scanner.setScannerListener((barcode, format) -> {
                Intent result = new Intent();
                result.putExtra(BARCODE_EXTRA, barcode);
                result.putExtra(BARCODE_FORMAT_EXTRA, format.name());
                setResult(RESULT_OK, result);
                finish();
            });
        }
    }
}
