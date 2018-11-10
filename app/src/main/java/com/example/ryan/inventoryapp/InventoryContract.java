package com.example.ryan.inventoryapp;

import android.net.Uri;
import android.provider.BaseColumns;

// Code is reference from the official Android website:https://developer.android.com/training/data-storage/sqlite#java and helpful understanding from this stackoverflow link:https://stackoverflow.com/questions/17451931/how-to-use-a-contract-class-in-android
public class InventoryContract {


    private InventoryContract() {
    }


    //Creating the names of the table..organization is key
    /* Inner class that defines the table contents */
    public static class InventoryEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String INVENTORY_ITEMS = "InventoryList";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INVENTORY_ITEMS);


        public static final String TABLE_NAME = "InventoryList";
        public static final String NAME_COLUMN = "Product_Name";
        public static final String PRICE_COLUMN = "Price";
        public static final String QUANTITY_COLUMN = "Quantity";
        public static final String SUPPLIER_NAME = "Supplier_Name";
        public static final String SUPPLIER_PHONE_NUMBER_COLUMN = "Supplier_Phone_Number";

    }

}
