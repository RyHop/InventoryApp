package com.example.ryan.inventoryapp;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements AddDialogBox.AddDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.NAME_COLUMN, "dummy");
        values.put(InventoryContract.InventoryEntry.PRICE_COLUMN, "2");
        values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, 1);
        values.put(InventoryContract.InventoryEntry.SUPPLIER_NAME, "hello");
        values.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN, "help");

        // Insert a new row for item into the provider using the ContentResolver.
        // Receive the new content URI that will allow us to access the item's data in the future.
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showAddDialog();
            }
        });

    }



    public void showAddDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddDialogBox();
        dialog.show(getFragmentManager(), "AddDialogFragment");
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database. Got this method from the https://github.com/udacity/ud845-Pets/blob/a53dd16846606a7980c8569fc23205e35fa85ea2/app/src/main/java/com/example/android/pets/CatalogActivity.java
     * Just want to use this method to see if database is created and can read from it.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.NAME_COLUMN,
                InventoryContract.InventoryEntry.PRICE_COLUMN,
                InventoryContract.InventoryEntry.QUANTITY_COLUMN,
                InventoryContract.InventoryEntry.SUPPLIER_NAME,
                InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN
        };
        Cursor cursor = getContentResolver().query(InventoryContract.InventoryEntry.CONTENT_URI, projection, null, null, null);


        cursor.close();
    }


    @Override
    public void onDialogDoneClick(DialogFragment dialog, String name, String price, int quantity, String supplierName, String supplierNumber) {

        //Already checked for null values, so Insert into database
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.NAME_COLUMN, name);
        values.put(InventoryContract.InventoryEntry.PRICE_COLUMN, price);
        values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, quantity);
        values.put(InventoryContract.InventoryEntry.SUPPLIER_NAME, supplierName);
        values.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN, supplierNumber);

        // Insert a new row for item into the provider using the ContentResolver.
        // Receive the new content URI that will allow us to access the item's data in the future.
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_success),
                    Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_fail),
                    Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();

    }
}
