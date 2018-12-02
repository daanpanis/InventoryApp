package com.daan.android.inventoryapp;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.IntentFilter;

import com.daan.android.inventoryapp.receivers.SavingBroadcastReceiver;
import com.daan.android.inventoryapp.slider.PicassoImageLoadingService;
import com.daan.android.inventoryapp.viewmodel.GlobalViewModel;

import ss.com.bannerslider.Slider;

import static com.daan.android.inventoryapp.utils.Constants.ACTION_SAVING;

public class InventoryApplication extends Application {

    private static InventoryApplication instance;

    public static InventoryApplication getInstance() {
        return instance;
    }

    public static GlobalViewModel getGlobalModel() {
        if (getInstance() == null) {
            return null;
        }
        return getInstance().globalViewModel;
    }

    private GlobalViewModel globalViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Slider.init(new PicassoImageLoadingService());
//        globalViewModel = new GlobalViewModel();
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this);
        ViewModelStore store = new ViewModelStore();
        ViewModelProvider viewModelProvider = new ViewModelProvider(store, factory);
        globalViewModel = viewModelProvider.get(GlobalViewModel.class);

        getInstance().registerReceiver(new SavingBroadcastReceiver(), new IntentFilter(ACTION_SAVING));
    }
}
