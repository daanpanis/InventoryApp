package com.daan.android.inventoryapp.models;

import com.bumptech.glide.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private String ownerId;
    private String name;
    private String barcode;
    private String barcodeFormat;
    private List<String> imageUrls = new ArrayList<>();
    private String description;

    public Item(String ownerId) {
        this.ownerId = ownerId;
    }

    public Item() {
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setImageUrls(List<String> imageUrls) {
        Preconditions.checkNotNull(imageUrls);
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(String barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
