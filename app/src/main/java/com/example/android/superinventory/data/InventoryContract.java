package com.example.android.superinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.superinventory.R;

import butterknife.BindString;
import butterknife.ButterKnife;

public final class InventoryContract {

    // Create necessary object for further usage
    public static final String CONTENT_AUTHORITY = "com.example.android.superinventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    // Prevent conflict contract class
    private InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        // The MIME type of the {@link #CONTENT_URI} for a list of pets.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        // The MIME type of the {@link #CONTENT_URI} for a single pet.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        // The content URI to access the pet data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        // Name of the database
        public final static String TABLE_NAME = "inventory";

        // Unique ID number for this database
        public final static String _ID = BaseColumns._ID;

        // Name of the product
        public final static String COLUMN_PRODUCT_NAME = "name";

        // Price of the product
        public final static String COLUMN_PRODUCT_PRICE = "price";

        // Quantity of the product
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        // Supplier name of the product
        public final static String COLUMN_SUPPLIER_NAME = "supplier";

        // Supplier phone number of the product
        public final static String COLUMN_SUPPLIER_PHONE = "phone";
    }
}
