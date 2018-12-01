package com.daan.android.inventoryapp.slider;

import android.support.annotation.DrawableRes;
import android.support.v4.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ImageSliderAdapter extends SliderAdapter {

    private List<String> imageUrls;
    @DrawableRes
    private final int placeHolderImage;

    public ImageSliderAdapter(int placeHolderImage) {
        this(placeHolderImage, new ArrayList<>());
    }


    public ImageSliderAdapter(@DrawableRes int placeHolderImage, List<String> imageUrls) {
        this.placeHolderImage = placeHolderImage;
        this.imageUrls = imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        Preconditions.checkNotNull(imageUrls);
        this.imageUrls = imageUrls;
    }

    @Override
    public int getItemCount() {
        return Math.max(1, imageUrls.size());
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        if (imageUrls.size() == 0) {
            viewHolder.bindImageSlide(placeHolderImage);
        } else {
            viewHolder.bindImageSlide(imageUrls.get(position));
        }
    }
}
