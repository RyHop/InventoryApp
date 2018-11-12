package com.example.ryan.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

// Code is reference from the official Android website:https://developer.android.com/training/data-storage/sqlite#java and helpful understanding from this stackoverflow link:https://stackoverflow.com/questions/17451931/how-to-use-a-contract-class-in-android
public class InventoryContract {


    private InventoryContract() {
    }


    //Creating the names of the table..organization is key
    /* Inner class that defines the table contents */
    public static class InventoryEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.ryan.inventoryapp.InventoryProvider";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String INVENTORY_ITEMS = "InventoryList";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INVENTORY_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + InventoryEntry.NAME_COLUMN;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + InventoryEntry.NAME_COLUMN;

        public static final String TABLE_NAME = "InventoryList";
        public static final String NAME_COLUMN = "Product_Name";
        public static final String PRICE_COLUMN = "Price";
        public static final String QUANTITY_COLUMN = "Quantity";
        public static final String SUPPLIER_NAME = "Supplier_Name";
        public static final String SUPPLIER_PHONE_NUMBER_COLUMN = "Supplier_Phone_Number";

    }

}
