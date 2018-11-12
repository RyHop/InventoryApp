package com.example.ryan.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InventoryCursorAdapter extends CursorAdapter {

   /* public interface changeQuantity{
        public void saleButtonClick();



    }*/

/*
    changeQuantity mListener;
*/

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
       /* try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (changeQuantity) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement AddDialogListener");
        }*/
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.nameOfProduct);
        TextView tvPrice = (TextView) view.findViewById(R.id.priceListViewTextBox);
        final TextView tvQuantity = (TextView) view.findViewById(R.id.quantityListViewTextBox);
        Button saleButton = (Button) view.findViewById(R.id.saleButton);
        Button editButton = (Button) view.findViewById(R.id.editButton);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.NAME_COLUMN));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRICE_COLUMN));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.QUANTITY_COLUMN));
        //Get the ID
        final long ID = cursor.getPosition() + 1;


        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrease Quantity by one
                if (quantity > 0) {
                    //
                    Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, ID);


                    // We can decrease it by one
                    int newQuant = decreaseQuantity(context, quantity, uri);
                    tvQuantity.setText(String.valueOf(newQuant));


                } else {
                    Toast.makeText(context, R.string.canNotDecrease, Toast.LENGTH_SHORT).show();
                }


            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // Populate fields with extracted properties
        tvName.setText(name);
        tvPrice.setText(price);
        tvQuantity.setText(String.valueOf(quantity));


    }

    private int decreaseQuantity(Context context, int quantity, Uri uri) {
        int mQuantity = quantity;
        mQuantity -= 1;

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.QUANTITY_COLUMN, mQuantity);

        // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
        // and pass in the new ContentValues. Pass in null for the selection and selection args
        // because mCurrentPetUri will already identify the correct row in the database that
        // we want to modify.
        int rowsAffected = context.getContentResolver().update(uri, values, null, null);

        if (rowsAffected == 0) {
            //Fail
            Toast.makeText(context, context.getString(R.string.editor_update_fail),
                    Toast.LENGTH_SHORT).show();


        } else {
            //Successfull
            Toast.makeText(context, context.getString(R.string.editor_update_successful),
                    Toast.LENGTH_SHORT).show();
        }


        return mQuantity;
    }
}
