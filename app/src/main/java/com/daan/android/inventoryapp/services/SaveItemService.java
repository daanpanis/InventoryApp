package com.daan.android.inventoryapp.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

import com.daan.android.inventoryapp.firebase.ItemStoreService;
import com.daan.android.inventoryapp.viewmodel.CreateItemViewModel;

import static com.daan.android.inventoryapp.utils.Constants.ACTION_SAVING;
import static com.daan.android.inventoryapp.utils.Constants.ITEM_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.SAVING_EXTRA;

public class SaveItemService extends IntentService {

    public SaveItemService() {
        super("SaveItemService");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(Intent intent) {
        if (!intent.hasExtra(ITEM_EXTRA)) {
            return;
        }
        CreateItemViewModel model = intent.getParcelableExtra(ITEM_EXTRA);
        getApplicationContext().sendBroadcast(buildBroadcastIntent(true));
        ItemStoreService.getInstance().addItem(model).blockingGet();
        getApplicationContext().sendBroadcast(buildBroadcastIntent(false));
    }

    private Intent buildBroadcastIntent(boolean saving) {
        Intent intent = new Intent();
        intent.setAction(ACTION_SAVING);
        intent.putExtra(SAVING_EXTRA, saving);
        return intent;
    }
}
