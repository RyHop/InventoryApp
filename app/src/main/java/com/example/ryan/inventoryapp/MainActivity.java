package com.example.ryan.inventoryapp;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements AddDialogBox.AddDialogListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final String LogTag = MainActivity.class.getSimpleName();

    private final static int itemLoader = 0;
    InventoryCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.itemsListView);


        View emptyView = (TextView) findViewById(R.id.emptyView);
        lvItems.setEmptyView(emptyView);

        myAdapter = new InventoryCursorAdapter(this, null);

        //Creates a listoner if clicked

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open up product page and send the uri
                Intent intent = new Intent(MainActivity.this, DetailedPage.class);

                Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                intent.setData(uri);
                startActivity(intent);
            }
        });

        lvItems.setAdapter(myAdapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
        //Initialize Header
        getLoaderManager().initLoader(itemLoader, null, this);

    }


    public void showAddDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddDialogBox();
        dialog.show(getFragmentManager(), "AddDialogFragment");
    }

    @Override
    public void onDialogDoneClick(DialogFragment dialog, String name, String price, int quantity, String supplierName, String supplierNumber) {


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
            Toast.makeText(this, getString(R.string.editor_insert_fail),
                    Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_success),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.NAME_COLUMN,
                InventoryContract.InventoryEntry.PRICE_COLUMN,
                InventoryContract.InventoryEntry.QUANTITY_COLUMN

        };
        return new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);

    }
}
