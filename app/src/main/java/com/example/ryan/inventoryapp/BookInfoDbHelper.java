package com.example.ryan.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ryan.inventoryapp.BookInfoContract.BookEntry;


public class BookInfoDbHelper extends SQLiteOpenHelper {
    //Create Database command for CREATE
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BookEntry.TABLE_NAME + "(" +
                    BookEntry._ID + " INTEGER PRIMARY KEY, " +
                    BookEntry.NAME_COLUMN + " TEXT, " +
                    BookEntry.PRICE_COLUMN + " INTEGER, " +
                    BookEntry.QUANTITY_COLUMN + " INTEGER, " +
                    BookEntry.SUPPLIER_NAME + " TEXT, " +
                    BookEntry.SUPPLIER_PHONE_NUMBER_COLUMN + " TEXT);";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;


        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "BookInfo.db";

        public BookInfoDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            //Method calls when the database is created.
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

}
