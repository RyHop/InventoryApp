package com.example.ryan.inventoryapp;

import android.provider.BaseColumns;

// Code is reference from the official Android website:https://developer.android.com/training/data-storage/sqlite#java and helpful understanding from this stackoverflow link:https://stackoverflow.com/questions/17451931/how-to-use-a-contract-class-in-android
public class BookInfoContract {

    private BookInfoContract() {
    }


    //Creating the names of the table..organization is key
    /* Inner class that defines the table contents */
    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "BookList";
        public static final String NAME_COLUMN = "Product_Name";
        public static final String PRICE_COLUMN = "Price";
        public static final String QUANTITY_COLUMN = "Quantity";
        public static final String SUPPLIER_NAME = "Supplier_Name";
        public static final String SUPPLIER_PHONE_NUMBER_COLUMN = "Supplier_Phone_Number";

    }

}
