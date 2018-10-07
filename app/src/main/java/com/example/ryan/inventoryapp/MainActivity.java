package com.example.ryan.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    BookInfoDbHelper mDbHelper = new BookInfoDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayDatabaseInfo();
    }

    // Cursor reading the most recent data inserted
    public void readRecentDataDB(View view) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                BookInfoContract.BookEntry.NAME_COLUMN
        };



        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BookInfoContract.BookEntry.NAME_COLUMN + " DESC";

        // I just want to look at the name
        Cursor cursor = db.query(
                BookInfoContract.BookEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        String lastItem = null;

        try{
            while(cursor.moveToNext()) {
                lastItem = cursor.getString(cursor.getColumnIndex(BookInfoContract.BookEntry.NAME_COLUMN));
            }

        }finally {
            cursor.close();
            Toast.makeText(this,lastItem + " is the last inserted book.",Toast.LENGTH_SHORT).show();

        }
    }



    //Method call when the button is hit. We are going to insert data into the. From the offical Android documentation website
    public void InsertingData(View view){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookInfoContract.BookEntry.NAME_COLUMN, "The Brief History of Time");
        values.put(BookInfoContract.BookEntry.PRICE_COLUMN, 5);
        values.put(BookInfoContract.BookEntry.QUANTITY_COLUMN,10);
        values.put(BookInfoContract.BookEntry.SUPPLIER_NAME, "We Got The Books");
        values.put(BookInfoContract.BookEntry.SUPPLIER_PHONE_NUMBER_COLUMN,"662-313-4923");
        db.insert(BookInfoContract.BookEntry.TABLE_NAME, null, values);


      // Update the the display
        displayDatabaseInfo();
    }
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database. Got this method from the https://github.com/udacity/ud845-Pets/blob/a53dd16846606a7980c8569fc23205e35fa85ea2/app/src/main/java/com/example/android/pets/CatalogActivity.java
     * Just want to use this method to see if database is created and can read from it.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookInfoContract.BookEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.testingDatabaseString);
            displayView.setText("Number of rows in my database table: " + cursor.getCount());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


}
