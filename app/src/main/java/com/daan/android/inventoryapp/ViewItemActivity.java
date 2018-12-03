package com.daan.android.inventoryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.daan.android.inventoryapp.firebase.ItemStoreService;
import com.daan.android.inventoryapp.models.Item;
import com.daan.android.inventoryapp.slider.ImageSliderAdapter;
import com.daan.android.inventoryapp.utils.ImageUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ss.com.bannerslider.Slider;

import static com.daan.android.inventoryapp.utils.Constants.ITEM_ID_EXTRA;
import static com.daan.android.inventoryapp.utils.ImageUtils.getBarcodeFormat;

public class ViewItemActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private ImageSliderAdapter adapter;

    @BindView(R.id.banner_slider)
    Slider slider;
    @BindView(R.id.tv_item_name)
    TextView itemName;
    @BindView(R.id.tv_item_description)
    TextView itemDescription;
    @BindView(R.id.iv_item_barcode)
    ImageView itemBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        slider.setAdapter((adapter = new ImageSliderAdapter(R.drawable.placeholder_16_9)));

        if (getIntent().hasExtra(ITEM_ID_EXTRA)) {
            String itemId = getIntent().getStringExtra(ITEM_ID_EXTRA);
            disposables.add(ItemStoreService.getInstance().getItemById(itemId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(document -> {
                        if (document == null || !document.exists()) {
                            finish();
                            return;
                        }
                        updateUI(document.toObject(Item.class));
                    }));
        }
    }

    private void updateUI(Item item) {
        if (item == null) {
            finish();
            return;
        }
        adapter.setImageUrls(item.getImageUrls());
        slider.adapter.notifyDataSetChanged();

        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());

        BarcodeFormat format = getBarcodeFormat(item.getBarcodeFormat());
        if (item.getBarcode() != null && format != null) {
            disposables.add(ImageUtils.generateBarcode(item.getBarcode(), format, new Dimension(1000, 200))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> itemBarcode.setImageBitmap(bitmap)));
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
