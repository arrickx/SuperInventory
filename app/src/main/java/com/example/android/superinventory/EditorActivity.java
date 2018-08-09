package com.example.android.superinventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.superinventory.data.InventoryContract.InventoryEntry;
import com.example.android.superinventory.data.InventoryDbHelper;

import org.w3c.dom.Text;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

//Create new inventory or edit the existing inventory
public class EditorActivity extends AppCompatActivity {

    //Bind view with Butter Knife for cleaner code.
    @BindView(R.id.edit_product_name) EditText mProductNameEditText;
    @BindView(R.id.edit_product_price) EditText mProductPriceEditText;
    @BindView(R.id.edit_product_quantity) EditText mProductQuantityEditText;
    @BindView(R.id.edit_supplier_name) EditText mSupplierNameEditText;
    @BindView(R.id.edit_supplier_phone) EditText mSupplierPhoneEditText;
    @BindString(R.string.warning_message) String warningMessage;
    @BindString(R.string.error_message) String errorMessage;
    @BindString(R.string.success_message) String successMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
    }

    //Get user input from editor and save new inventory into database.
    private void insertInventory() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String productPriceString = mProductPriceEditText.getText().toString().trim();
        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        //Create database helper
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPriceString);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, successMessage + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    //Check if EditText is empty or not
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    //Check if any of the EditText is empty, if so warning and do not insert the inventory.
    boolean checkData(){
        if(isEmpty(mProductNameEditText)||isEmpty(mProductPriceEditText)||
                isEmpty(mProductQuantityEditText)||isEmpty(mSupplierNameEditText)||
                isEmpty(mSupplierPhoneEditText)){
            if (isEmpty(mProductNameEditText)) {
                mProductNameEditText.setError(warningMessage);
            }
            if (isEmpty(mProductPriceEditText)) {
                mProductPriceEditText.setError(warningMessage);
            }
            if (isEmpty(mProductQuantityEditText)) {
                mProductQuantityEditText.setError(warningMessage);
            }
            if (isEmpty(mSupplierNameEditText)) {
                mSupplierNameEditText.setError(warningMessage);
            }
            if (isEmpty(mSupplierPhoneEditText)) {
                mSupplierPhoneEditText.setError(warningMessage);
            }
            return false;
        } else {
            insertInventory();
            finish();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //If all data included, save inventory into database.
                checkData();
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}