package com.daan.android.inventoryapp;

import android.app.Application;

import com.daan.android.inventoryapp.slider.PicassoImageLoadingService;

import ss.com.bannerslider.Slider;

public class InventoryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Slider.init(new PicassoImageLoadingService());
    }
}
