package com.example.ryan.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class InventoryProvider extends ContentProvider {


    // Log tag to help debug
    public static String LOG_TAG = InventoryProvider.class.getSimpleName();
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int InventoryList = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int InventoryList_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(InventoryContract.InventoryEntry.CONTENT_AUTHORITY, InventoryContract.InventoryEntry.TABLE_NAME, 100);
        sUriMatcher.addURI(InventoryContract.InventoryEntry.CONTENT_AUTHORITY, InventoryContract.InventoryEntry.TABLE_NAME + "/#", 101);


    }


    private InventoryDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDBHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //See what we need to query
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;
        switch (match) {
            case InventoryList:
                // We are querying for the whole table
                // To access our database, we instantiate our subclass of SQLiteOpenHelper
                // and pass the context, which is the current activity.

                // Create and/or open a database to read from it
                cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case InventoryList_ID:
                // Create and/or open a database to read from it

                // We are querying for that one item
                selection = InventoryContract.InventoryEntry._ID + "?=";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                //No match found
                throw new IllegalArgumentException("Can not query results" + uri);


        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case InventoryList:
                return InventoryContract.InventoryEntry.CONTENT_LIST_TYPE;
            case InventoryList_ID:
                return InventoryContract.InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case InventoryList:
                return insertInventoryItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertInventoryItem(Uri uri, ContentValues values) {


        //Sanity Checks

        // We got to make the database write
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        // TODO: Insert a new item into the pets database table with the given ContentValues


        long id = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }


        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Track the number of rows that were deleted
        int rowsDeleted;

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        final int match = sUriMatcher.match(uri);
        switch (match) {
            case InventoryList:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case InventoryList_ID:
                // Delete a single row given by the ID in the URI
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case InventoryList:
                return updateItem(uri, values, selection, selectionArgs);
            case InventoryList_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        if (values.size() == 0) {
            return 0;
        }
        // check that the name value is not null.
        if (values.containsKey(InventoryContract.InventoryEntry.NAME_COLUMN)) {
            String name = values.getAsString(InventoryContract.InventoryEntry.NAME_COLUMN);
            if (name == null) {
                throw new IllegalArgumentException("There is no name.");
            }
        }
        // check that the price value is not null.
        if (values.containsKey(InventoryContract.InventoryEntry.PRICE_COLUMN)) {
            String price = values.getAsString(InventoryContract.InventoryEntry.NAME_COLUMN);
            if (price == null) {
                throw new IllegalArgumentException("There is no price.");
            }
        }
        // check that the name value is not null.
        if (values.containsKey(InventoryContract.InventoryEntry.QUANTITY_COLUMN)) {
            String quantity = values.getAsString(InventoryContract.InventoryEntry.NAME_COLUMN);
            if (quantity == null) {
                throw new IllegalArgumentException("There is quantity.");
            }
        }
        // check that the name value is not null.
        if (values.containsKey(InventoryContract.InventoryEntry.SUPPLIER_NAME)) {
            String sName = values.getAsString(InventoryContract.InventoryEntry.SUPPLIER_NAME);
            if (sName == null) {
                throw new IllegalArgumentException("There is no supplier name.");
            }
        }
        // check that the name value is not null.
        if (values.containsKey(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN)) {
            String sNumber = values.getAsString(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN);
            if (sNumber == null) {
                throw new IllegalArgumentException("There is no supplier number.");
            }
        }

        // TODO: Update the selected pets in the pets database table with the given ContentValues
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;


    }

}
