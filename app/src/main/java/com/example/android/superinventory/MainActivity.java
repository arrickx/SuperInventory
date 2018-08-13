package com.example.android.superinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.superinventory.data.InventoryContract.InventoryEntry;
import com.example.android.superinventory.data.InventoryDbHelper;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //Bind view with Butter Knife for cleaner code.
    @BindView(R.id.list) ListView inventoryListView;
    @BindView(R.id.empty_view) View emptyView;
    @BindString(R.string.dummy_product_name) String dummyProductName;
    @BindString(R.string.dummy_supplier_name) String dummySupplierName;
    @BindString(R.string.dummy_supplier_phone) String dummySupplierPhone;
    @BindString(R.string.display_msg) String displayMessage;
    @BindString(R.string.success_message) String successMessage;
    @BindString(R.string.error_message) String errorMessage;
    @BindString(R.string.log_tag_1) String logTag;

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new InventoryDbHelper(this);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        inventoryListView.setEmptyView(emptyView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    //Temporary helper method to display information about the database
    private void displayDatabaseInfo() {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE,

        };

        // Perform a query on the inventory table
        Cursor cursor = getContentResolver().query(InventoryEntry.CONTENT_URI, projection, null, null,null);

        // Setup an Adapter to create a list item for each row of data in the Cursor.
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
        inventoryListView.setAdapter(adapter);
    }

    private void insertInventory() {
        // Create a ContentValues object where column names are the keys,
        // and the sample attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, dummyProductName);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, 699.99);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 3);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, dummySupplierName);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, dummySupplierPhone);

        // Insert a new row for dummyProductName into the provider using the ContentResolver.
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, successMessage , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertInventory();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all:
                // Do nothing for now
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
