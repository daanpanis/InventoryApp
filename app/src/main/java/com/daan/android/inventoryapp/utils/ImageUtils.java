package com.daan.android.inventoryapp.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private ImageUtils() {
    }

}
