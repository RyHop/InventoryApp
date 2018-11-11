package com.example.ryan.inventoryapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// All info is from the official Android Dialog box tutorial.

public class AddDialogBox extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddDialogListener {
        public void onDialogDoneClick(DialogFragment dialog, String nameString, String priceString, int quantityInt, String supplierNameString, String supplierNumberString);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //Got solution or how to grab view texts from this website: https://stackoverflow.com/questions/31153018/get-edittext-value-from-alertdialog-builder
        View mView = inflater.inflate(R.layout.add_inventor_item_dialog_box_layout, null);
        final EditText name = (EditText) mView.findViewById(R.id.NameEditText);
        final EditText price = (EditText) mView.findViewById(R.id.PriceEditText);
        final EditText quantity = (EditText) mView.findViewById(R.id.QuantityEditText);
        final EditText supplierName = (EditText) mView.findViewById(R.id.SupplierNameEditText);
        final EditText supplierNumber = (EditText) mView.findViewById(R.id.SupplierNumberEditText);

        builder.setView(mView)

                // Add action buttons
                .setPositiveButton(R.string.DoneButtonLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // check to see if data is null

                        String nameString = name.getText().toString().trim();
                        String priceString = price.getText().toString().trim();
                        String quantityString = quantity.getText().toString().trim();
                        String supplierNameString = supplierName.getText().toString().trim();
                        String supplierNumberString = supplierNumber.getText().toString().trim();

                        // Price should be an float..a decimal or float
                        // Quantity should be an integer, a positive integer
                        double priceDouble = 0;
                        int quantityInt = 0;

                        try {
                            priceDouble = Double.valueOf(priceString);
                            quantityInt = Integer.valueOf(quantityString);
                        } catch (Exception e) {


                        }

                        if (!nameString.equals("") && !priceString.equals("") && !quantityString.equals("") && !supplierNameString.equals("") && !supplierNumberString.equals("")) {


                            // Check to see if quantity and price is above or equal zero
                            if (quantityInt > 0 && priceDouble >= 0) {
                                mListener.onDialogDoneClick(AddDialogBox.this, nameString, priceString, quantityInt, supplierNameString, supplierNumberString);

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.positiveMatters, Toast.LENGTH_SHORT).show();

                            }
                            // Add items if all values are not null


                        } else {

                            Toast.makeText(getActivity().getApplicationContext(), R.string.showNullValuesText, Toast.LENGTH_SHORT).show();

                        }



                    }
                })
                .setNegativeButton(R.string.CancelButtonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(AddDialogBox.this);
                    }
                });
        return builder.create();
    }
}
