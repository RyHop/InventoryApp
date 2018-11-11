package com.example.ryan.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class DetailedPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int itemLoader = 0;


    TextView nameTextBox;
    TextView priceTextBox;
    TextView quantityTextBox;
    TextView supplierNameTextBox;
    TextView supplierNumberTextBox;
    Button orderButton;
    Button increaseButton;
    Button decreaseButton;
    Button deleteButton;
    Uri contentUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);

        String uri = getIntent().getStringExtra("theUri");
        contentUri = Uri.parse(uri);

        //lets get the views as variables!
        nameTextBox = (TextView) findViewById(R.id.nameTextBox);
        priceTextBox = (TextView) findViewById(R.id.priceTextBox);
        quantityTextBox = (TextView) findViewById(R.id.quantityListViewTextBox);
        supplierNameTextBox = (TextView) findViewById(R.id.supplierNameTextBox);
        supplierNumberTextBox = (TextView) findViewById(R.id.supplierNumberTextBox);

        orderButton = (Button) findViewById(R.id.orderButton);
        increaseButton = (Button) findViewById(R.id.increaseQuantityButton);
        decreaseButton = (Button) findViewById(R.id.decreaseQuantityButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        getLoaderManager().initLoader(itemLoader, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.NAME_COLUMN,
                InventoryContract.InventoryEntry.PRICE_COLUMN,
                InventoryContract.InventoryEntry.QUANTITY_COLUMN,
                InventoryContract.InventoryEntry.SUPPLIER_NAME,
                InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN
        };
        return new CursorLoader(this, contentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Determine the column index of the column named "word"
        if (data.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.NAME_COLUMN);
            int priceColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.PRICE_COLUMN);
            int quantityColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.QUANTITY_COLUMN);
            int supplierNameColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.SUPPLIER_NAME);
            int supplierNumberColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN);

            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            String price = data.getString(priceColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            String supplierName = data.getString(supplierNameColumnIndex);
            String supplierNumber = data.getString(supplierNumberColumnIndex);

            nameTextBox.setText(name);
            priceTextBox.setText(price);
            quantityTextBox.setText(String.valueOf(quantity));
            supplierNameTextBox.setText(supplierName);
            supplierNumberTextBox.setText(supplierNumber);
//oops

        } else {

            throw new IllegalArgumentException("Something wrong with cursor");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing

    }
}
