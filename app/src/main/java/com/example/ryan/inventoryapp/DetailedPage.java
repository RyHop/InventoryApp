package com.example.ryan.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int itemLoader1 = 0;


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

    String supplierNumber;
    int quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);

        Intent intent = getIntent();
        contentUri = intent.getData();


        //lets get the views as variables!
        nameTextBox = (TextView) findViewById(R.id.nameTextBox);
        priceTextBox = (TextView) findViewById(R.id.priceTextBox);
        quantityTextBox = (TextView) findViewById(R.id.QuantityTextBox);
        supplierNameTextBox = (TextView) findViewById(R.id.supplierNameTextBox);
        supplierNumberTextBox = (TextView) findViewById(R.id.supplierNumberTextBox);

        orderButton = (Button) findViewById(R.id.orderButton);
        increaseButton = (Button) findViewById(R.id.increaseQuantityButton);
        decreaseButton = (Button) findViewById(R.id.decreaseQuantityButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the phone number, and send an intent to call app
                dialPhoneNumber(supplierNumber);

            }
        });
        increaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Increase the quantity
                int mQuantity = quantity;
                mQuantity += 1;
                ContentValues values = new ContentValues();
                values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, mQuantity);
                int rowsAffected = getContentResolver().update(contentUri, values, null, null);
                if (rowsAffected == 0) {
                    //Fail
                    Toast.makeText(getApplicationContext(), getString(R.string.editor_update_fail),
                            Toast.LENGTH_SHORT).show();


                } else {
                    //Successfull
                    Toast.makeText(getApplicationContext(), getString(R.string.editor_update_successful),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    int mQuantity = quantity;
                    mQuantity -= 1;

                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, mQuantity);

                    // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPetUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(contentUri, values, null, null);

                    if (rowsAffected == 0) {
                        //Fail
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_update_fail),
                                Toast.LENGTH_SHORT).show();


                    } else {
                        //Successfull
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_update_successful),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.canNotDecrease, Toast.LENGTH_SHORT).show();

                }


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getLoaderManager().initLoader(itemLoader1, null, this);

    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
            quantity = data.getInt(quantityColumnIndex);
            String supplierName = data.getString(supplierNameColumnIndex);
            supplierNumber = data.getString(supplierNumberColumnIndex);



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
