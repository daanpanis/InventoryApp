package com.daan.android.inventoryapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.daan.android.inventoryapp.firebase.ItemStoreService;
import com.daan.android.inventoryapp.utils.Loader;

import static com.daan.android.inventoryapp.utils.Constants.BARCODE_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.BARCODE_FORMAT_EXTRA;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white);
            actionBar.setTitle(R.string.search_results_title);
        }

        ItemListFragment itemList = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_item_list);
        if (itemList != null && getIntent().hasExtra(BARCODE_EXTRA) && getIntent().hasExtra(BARCODE_FORMAT_EXTRA)) {
            String barcode = getIntent().getStringExtra(BARCODE_EXTRA);
            String barcodeFormat = getIntent().getStringExtra(BARCODE_FORMAT_EXTRA);
            Loader.showSpinner(this);
            itemList.setItemsObservable(ItemStoreService.getInstance()
                    .getItemsByBarcode(barcode, barcodeFormat)
                    .doOnComplete(() -> Loader.removeSpinner(this))
                    .doOnNext(documentChanges -> Loader.removeSpinner(this)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
