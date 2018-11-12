package com.example.ryan.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

public class DetailedPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int itemLoader1 = 1;


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


                    // we want to modify or update one item.
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

                showDeleteConfirmationDialog();
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

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Delete the item
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Clicking don't want to delete it.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {

        //Getting the feedback of deletion
        int rowsDeleted = getContentResolver().delete(contentUri, null, null);

        //See if deleted
        if (rowsDeleted == 0) {
            Toast.makeText(this, getString(R.string.ItemNotDeleted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.DeleteSuccess), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(DetailedPage.this, MainActivity.class);
        startActivity(intent);

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

        } else {

            //Send back to MainActivity if data is something else, say deleted.
            Intent intent = new Intent(DetailedPage.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing


    }
}
