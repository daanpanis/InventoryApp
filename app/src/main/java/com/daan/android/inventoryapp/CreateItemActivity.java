package com.daan.android.inventoryapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

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
import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import java8.util.stream.StreamSupport;
import ss.com.bannerslider.Slider;

public class CreateItemActivity extends AppCompatActivity {

    private CreateItemViewModel model;
    private ImageSliderAdapter adapter;

    @BindView(R.id.banner_slider)
    Slider slider;
    @BindView(R.id.et_item_name)
    EditText itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);
        model = ViewModelProviders.of(this).get(CreateItemViewModel.class);

        slider.setAdapter((adapter = new ImageSliderAdapter(R.drawable.placeholder_16_9, new ArrayList<>())));
        itemName.clearFocus();

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

    private File getTempFolder() {
        return new File(getFilesDir(), "temp");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefStreams.of(getTempFolder().listFiles()).forEach(File::delete);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                List<String> imageUrls = model.getImageUrls().getValue();
                Objects.requireNonNull(imageUrls).add(image.getPath());
                model.getImageUrls().postValue(imageUrls);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnTextChanged(R.id.et_item_name)
    void onNameChanged() {
        if (getCurrentFocus() == itemName) {
            model.getItemName().postValue(itemName.getText().toString());
        }
    }
}
