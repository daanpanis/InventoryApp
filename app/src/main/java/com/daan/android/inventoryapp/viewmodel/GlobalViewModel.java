package com.daan.android.inventoryapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class GlobalViewModel extends ViewModel {

    private MutableLiveData<Boolean> saving;

    public MutableLiveData<Boolean> isSaving() {
        if (saving == null) {
            saving = new MutableLiveData<>();
        }
        return saving;
    }

}
