package com.example.android.superinventory;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Loader;
import android.app.LoaderManager;
import com.example.android.superinventory.data.InventoryContract.InventoryEntry;
import android.app.AlertDialog;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

//Create new inventory or edit the existing inventory
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Bind view with Butter Knife for cleaner code.
    @BindView(R.id.edit_product_name) EditText mProductNameEditText;
    @BindView(R.id.edit_product_price) EditText mProductPriceEditText;
    @BindView(R.id.edit_product_quantity) EditText mProductQuantityEditText;
    @BindView(R.id.edit_supplier_name) EditText mSupplierNameEditText;
    @BindView(R.id.edit_supplier_phone) EditText mSupplierPhoneEditText;
    @BindString(R.string.warning_message) String warningMessage;
    @BindString(R.string.error_message) String errorMessage;
    @BindString(R.string.success_message) String successMessage;
    @BindString(R.string.error_message_update) String errorUpdate;
    @BindString(R.string.success_message_update) String successUpdate;
    @BindString(R.string.add_an_inventory) String addInventory;
    @BindString(R.string.edit_an_inventory) String editInventory;
    @BindString(R.string.empty_string) String nothing;

    // Identifier for the inventory data loader
    private static final int EXISTING_INVENTORY_LOADER = 0;

    // Content URI for the existing inventory
    private Uri mCurrentUri;

    // Boolean flag that keeps track of whether the item has been edited (true) or not (false)
    private boolean mHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        // Examine the intent that was used to launch this activity,
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        // If the intent DOES NOT contain a inventory content URI, then we know that we are
        // creating a new inventory.
        if (mCurrentUri == null) {
            // This is a new inventory, so change the app bar to say "Add a inventory"
            setTitle(addInventory);

        } else {
            // Otherwise this is an existing inventory, so change app bar to say "Edit inventory"
            setTitle(editInventory);

            // Initialize a loader to read the inventory data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        // Check if it's edited or not.
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductPriceEditText.setOnTouchListener(mTouchListener);
        mProductQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
    }

    //Get user input from editor and save inventory into database.
    private void saveInventory() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String productPriceString = mProductPriceEditText.getText().toString().trim();
        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and inventory attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPriceString);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);

        // Determine if this is a new or existing inventory by checking if mCurrentUri is null or not
        if (mCurrentUri == null) {
            // This is a NEW inventory, so insert a new inventory into the provider,
            // returning the content URI for the new inventory.
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, errorUpdate,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, successUpdate,
                            Toast.LENGTH_SHORT).show();
                }
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
            saveInventory();
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
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of inventory attributes
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            double productPrice = cursor.getDouble(productPriceColumnIndex);
            int productQuantity = cursor.getInt(productQuantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database
            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(Double.toString(productPrice));
            mProductQuantityEditText.setText(Integer.toString(productQuantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameEditText.setText(nothing);
        mProductPriceEditText.setText(nothing);
        mProductQuantityEditText.setText(nothing);
        mSupplierNameEditText.setText(nothing);
        mSupplierPhoneEditText.setText(nothing);
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}