package com.daan.android.inventoryapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.daan.android.inventoryapp.InventoryApplication;
import com.daan.android.inventoryapp.R;

import java.util.Objects;

import static com.daan.android.inventoryapp.utils.Constants.SAVING_EXTRA;

public class SavingBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(SAVING_EXTRA)) {
            Objects.requireNonNull(InventoryApplication.getGlobalModel()).isSaving()
                    .setValue(intent.getBooleanExtra(SAVING_EXTRA, false));
            if (intent.getBooleanExtra(SAVING_EXTRA, false)) {
                Toast.makeText(context, context.getString(R.string.saving_item), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getString(R.string.saved_item), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
