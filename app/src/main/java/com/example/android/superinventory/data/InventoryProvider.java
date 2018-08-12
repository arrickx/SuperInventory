package com.example.android.superinventory.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * {@link ContentProvider} for SuperInventory app.
 */
public class InventoryProvider extends ContentProvider {

    // Tag for the log messages
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    // Database helper object
    private InventoryDbHelper mDbHelper;

    // Initialize the provider and the database helper object.
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    // Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    // Returns the MIME type of data for the content URI.
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // Insert new data into the provider with the given ContentValues.
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    // Delete the data at the given selection and selection arguments.
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    // Updates the data at the given selection and selection arguments, with the new ContentValues.
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
