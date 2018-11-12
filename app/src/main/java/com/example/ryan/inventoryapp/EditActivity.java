package com.example.ryan.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int itemLoader2 = 2;

    EditText NameEdit;
    EditText PriceEdit;
    EditText QuantityEdit;
    EditText SupplierNameEdit;
    EditText SupplierPhoneEdit;
    Uri contentUri;
    Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        contentUri = intent.getData();

        setContentView(R.layout.activity_edit);
        NameEdit = (EditText) findViewById(R.id.NameEditText);
        PriceEdit = (EditText) findViewById(R.id.PriceEditText);
        QuantityEdit = (EditText) findViewById(R.id.QuantityEditText);
        SupplierNameEdit = (EditText) findViewById(R.id.SupplierNameEditText);
        SupplierPhoneEdit = (EditText) findViewById(R.id.SupplierNumberEditText);
        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theName = NameEdit.getText().toString().trim();
                String thePrice = PriceEdit.getText().toString().trim();
                String theCount = QuantityEdit.getText().toString().trim();
                String SupplierName = SupplierNameEdit.getText().toString().trim();
                String SupplierNumber = SupplierPhoneEdit.getText().toString().trim();

                // Validate the data
                if (validateData(theName, thePrice, theCount, SupplierName, SupplierNumber)) {
                    //Update teh Data or Save it.
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.NAME_COLUMN, theName);
                    values.put(InventoryContract.InventoryEntry.PRICE_COLUMN, thePrice);
                    values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, Integer.valueOf(theCount));
                    values.put(InventoryContract.InventoryEntry.SUPPLIER_NAME, SupplierName);
                    values.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER_COLUMN, SupplierName);

                    int rowsAffected = getContentResolver().update(contentUri, values, null, null);

                    if (rowsAffected == 0) {
                        //Fail
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_save_fail),
                                Toast.LENGTH_SHORT).show();


                    } else {
                        //Successfull
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_save_successful),
                                Toast.LENGTH_SHORT).show();
                    }

                    Intent intent2 = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent2);


                } else {
                    Toast.makeText(getApplicationContext(), R.string.DataInvalid, Toast.LENGTH_SHORT).show();
                }
            }
        });


        getLoaderManager().initLoader(itemLoader2, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateData(String theName, String thePrice, String theCount, String SupplierName, String SupplierNumber) {
        double priceDouble = 0;
        int quantityInt = 0;

        try {
            priceDouble = Double.valueOf(thePrice);
            quantityInt = Integer.valueOf(theCount);
        } catch (Exception e) {
            return false;

        }

        if (!theName.equals("") && !thePrice.equals("") && !theCount.equals("") && !SupplierName.equals("") && !SupplierNumber.equals("")) {


            // Check to see if quantity and price is above or equal zero
            if (quantityInt > 0 && priceDouble >= 0) {
                return true;
                //Update teh values
            } else {
                return false;
            }
            // Add items if all values are not null


        } else {
            return false;
        }


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


            NameEdit.setText(name);
            PriceEdit.setText(price);
            QuantityEdit.setText(String.valueOf(quantity));
            SupplierNameEdit.setText(supplierName);
            SupplierPhoneEdit.setText(supplierNumber);


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
}
