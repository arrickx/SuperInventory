package com.example.android.superinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.superinventory.data.InventoryContract.InventoryEntry;

//Manages database creation and version management.
public class InventoryDbHelper extends SQLiteOpenHelper {

    //Name of the database
    private static final String DATABASE_NAME = "inventory.db";

    //Database version.
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link InventoryDbHelper}.
     *
     * @param context of the app
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //This is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE =  "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL, "
                + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
