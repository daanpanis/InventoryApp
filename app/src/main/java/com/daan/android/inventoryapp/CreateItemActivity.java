package com.daan.android.inventoryapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.daan.android.inventoryapp.services.SaveItemService;
import com.daan.android.inventoryapp.slider.ImageSliderAdapter;
import com.daan.android.inventoryapp.viewmodel.CreateItemViewModel;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import java8.util.stream.StreamSupport;
import ss.com.bannerslider.Slider;

import static com.daan.android.inventoryapp.utils.Constants.BARCODE_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.BARCODE_FORMAT_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.ITEM_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.PC_BARCODE_SCANNER;

public class CreateItemActivity extends AppCompatActivity {

    private CreateItemViewModel model;
    private ImageSliderAdapter adapter;
    private CompositeDisposable disposables = new CompositeDisposable();

    @BindView(R.id.banner_slider)
    Slider slider;
    @BindView(R.id.et_item_name)
    EditText itemName;
    @BindView(R.id.et_item_barcode)
    EditText itemBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);
        model = ViewModelProviders.of(this).get(CreateItemViewModel.class);

        slider.setAdapter((adapter = new ImageSliderAdapter(R.drawable.placeholder_16_9, new ArrayList<>())));
        slider.setSelectedSlide(Objects.requireNonNull(model.getSliderIndex().getValue()));
        slider.setSlideChangeListener(position -> model.getSliderIndex().setValue(position));

        itemName.clearFocus();
        itemBarcode.clearFocus();

        itemName.setText(model.getItemName().getValue());
        itemBarcode.setText(model.getItemBarcode().getValue());
        model.getItemName().observe(this, s -> {
            if (getCurrentFocus() != itemName) {
                itemName.setText(s);
            }
        });
        model.getImageUrls().observe(this, paths -> {
            if (paths != null) {
                adapter.setImageUrls(StreamSupport.stream(paths).map(path -> new File(path).toURI().toString())
                        .collect(Collectors.toList()));
                slider.adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_save) {
         /*   disposables.add(ItemStoreService.getInstance()
                    .addItem(model)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> finish()));*/
            Intent serviceIntent = new Intent(this, SaveItemService.class);
            serviceIntent.putExtra(ITEM_EXTRA, model);
            startService(serviceIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_item_picture)
    void addImage() {
        ImagePicker
                .create(this)
                .returnMode(ReturnMode.ALL)
                .folderMode(false)
                .includeVideo(false)
                .single()
                .imageFullDirectory(getTempFolder().getPath())
                .start();
    }

    @OnClick(R.id.btn_scan_barcode)
    void scanBarcode() {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, PC_BARCODE_SCANNER);
    }

    private File getTempFolder() {
        return new File(getFilesDir(), "temp");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefStreams.of(getTempFolder().listFiles()).forEach(File::delete);
        disposables.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                List<String> imageUrls = model.getImageUrls().getValue();
                Objects.requireNonNull(imageUrls).add(image.getPath());
                model.getImageUrls().setValue(imageUrls);
            }
        } else if (requestCode == PC_BARCODE_SCANNER && resultCode == RESULT_OK) {
            String barcode = data.getStringExtra(BARCODE_EXTRA);
            String barcodeFormat = data.getStringExtra(BARCODE_FORMAT_EXTRA);
            itemBarcode.setText(barcode);
            model.getItemBarcode().setValue(barcode);
            model.getItemBarcodeFormat().setValue(barcodeFormat);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnTextChanged({R.id.et_item_name, R.id.et_item_barcode})
    void onTextChanged() {
        if (getCurrentFocus() == itemName) {
            model.getItemName().setValue(itemName.getText().toString());
        } else if (getCurrentFocus() == itemBarcode) {
            model.getItemBarcode().setValue(itemBarcode.getText().toString());
        }
    }
}
