package com.daan.android.inventoryapp.utils;

import android.graphics.Bitmap;
import android.support.v4.util.Preconditions;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.Observable;

public class ImageUtils {

    public static BarcodeFormat getBarcodeFormat(String format) {
        if (format == null) {
            return null;
        }
        for (BarcodeFormat barcodeFormat : BarcodeFormat.values()) {
            if (barcodeFormat.name().equals(format)) {
                return barcodeFormat;
            }
        }
        return null;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static Observable<Bitmap> generateBarcode(String content, BarcodeFormat format, Dimension size) {
        Preconditions.checkNotNull(content);
        Preconditions.checkNotNull(format);
        return Observable.fromCallable(() -> {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(content, format, size.getWidth(), size.getHeight());
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        });
    }

    private ImageUtils() {
    }

}
