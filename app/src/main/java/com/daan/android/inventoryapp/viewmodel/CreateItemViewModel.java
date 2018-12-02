package com.daan.android.inventoryapp.viewmodel;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class CreateItemViewModel extends ViewModel implements Parcelable {

    private MutableLiveData<String> itemName;
    private MutableLiveData<List<String>> imageUrls;
    private MutableLiveData<Integer> sliderIndex;
    private MutableLiveData<String> itemBarcode;
    private MutableLiveData<String> itemBarcodeFormat;

    public CreateItemViewModel() {
    }

    protected CreateItemViewModel(Parcel in) {
        Completable.fromAction(() -> {
            (this.itemName = new MutableLiveData<>()).setValue(in.readString());
            List<String> imageUrls = new ArrayList<>();
            in.readStringList(imageUrls);
            (this.imageUrls = new MutableLiveData<>()).setValue(imageUrls);
            (this.sliderIndex = new MutableLiveData<>()).setValue(in.readInt());
            (this.itemBarcode = new MutableLiveData<>()).setValue(in.readString());
            (this.itemBarcodeFormat = new MutableLiveData<>()).setValue(in.readString());
        }).subscribeOn(AndroidSchedulers.mainThread()).blockingAwait();
    }

    public static final Creator<CreateItemViewModel> CREATOR = new Creator<CreateItemViewModel>() {
        @Override
        public CreateItemViewModel createFromParcel(Parcel in) {
            return new CreateItemViewModel(in);
        }

        @Override
        public CreateItemViewModel[] newArray(int size) {
            return new CreateItemViewModel[size];
        }
    };

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

    public MutableLiveData<String> getItemBarcode() {
        if (itemBarcode == null) {
            itemBarcode = new MutableLiveData<>();
        }
        return itemBarcode;
    }

    public MutableLiveData<String> getItemBarcodeFormat() {
        if (itemBarcodeFormat == null) {
            itemBarcodeFormat = new MutableLiveData<>();
        }
        return itemBarcodeFormat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getItemName().getValue());
        dest.writeStringList(getImageUrls().getValue());
        dest.writeInt(getSliderIndex().getValue());
        dest.writeString(getItemBarcode().getValue());
        dest.writeString(getItemBarcodeFormat().getValue());
    }
}
