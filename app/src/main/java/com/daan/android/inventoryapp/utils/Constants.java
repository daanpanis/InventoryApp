package com.daan.android.inventoryapp.utils;

public class Constants {

    // Intent keys
    public static final String BARCODE_EXTRA = "barcode";
    public static final String BARCODE_FORMAT_EXTRA = "barcode_extra";
    public static final String ITEM_EXTRA = "item";
    public static final String SAVING_EXTRA = "saving";

    // Process codes
    public static int PC_BARCODE_SCANNER = 9865;

    // Firebase constants
    public static final String DB_ITEMS_COLLECTION = "items";
    public static final String DB_ITEM_OWNER_ID = "ownerId";
    public static final String DB_ITEM_BARCODE = "barcode";
    public static final String DB_ITEM_BARCODE_FORMAT = "barcodeFormat";

    public static final String STORAGE_BUCKET = "gs://inventory-app-3a584.appspot.com";
    public static final String ITEM_PICTURES_FOLDER = "item_pictures";

    // Intent filters
    public static final String ACTION_SAVING = "com.daan.android.inventoryapp.ACTION_SAVING";
    public static final String ACTION_SEARCH = "com.daan.android.inventoryapp.ACTION_SEARCH";

    private Constants() {
    }

}
