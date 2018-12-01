package com.daan.android.inventoryapp.viewmodel;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


public class CreateItemViewModel extends ViewModel {

    private MutableLiveData<String> itemName;
    private MutableLiveData<List<String>> imageUrls;
    private MutableLiveData<Integer> sliderIndex;

    public CreateItemViewModel() {
    }

    public MutableLiveData<String> getItemName() {
        if (itemName == null) {
            itemName = new MutableLiveData<>();
        }
        return itemName;
    }

    public MutableLiveData<List<String>> getImageUrls() {
        if (imageUrls == null) {
            imageUrls = new MutableLiveData<>();
            imageUrls.setValue(new ArrayList<>());
        }
        return imageUrls;
    }

    public MutableLiveData<Integer> getSliderIndex() {
        if (sliderIndex == null) {
            sliderIndex = new MutableLiveData<>();
            sliderIndex.setValue(1);
        }
        return sliderIndex;
    }
}
