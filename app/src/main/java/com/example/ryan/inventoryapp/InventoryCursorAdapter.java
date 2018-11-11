package com.example.ryan.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class InventoryCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.nameOfProduct);
        TextView tvPrice = (TextView) view.findViewById(R.id.priceListViewTextBox);
        TextView tvQuantity = (TextView) view.findViewById(R.id.quantityListViewTextBox);


        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.NAME_COLUMN));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRICE_COLUMN));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.QUANTITY_COLUMN));

        // Populate fields with extracted properties
        tvName.setText(name);
        tvPrice.setText(price);
        tvQuantity.setText(String.valueOf(quantity));

    }
}
