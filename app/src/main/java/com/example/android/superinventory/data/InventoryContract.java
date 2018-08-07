package com.example.android.superinventory.data;

import android.provider.BaseColumns;

public final class InventoryContract {
    //Prevent conflict contract class
    private InventoryContract() {}

    public static final class InventoryEntry implements BaseColumns {

        //Name of the database
        public final static String TABLE_NAME = "inventory";

        //Unique ID number for this database
        public final static String _ID = BaseColumns._ID;

        //Name of the product
        public final static String COLUMN_PRODUCT_NAME ="name";

        //Price of the product
        public final static String COLUMN_PRODUCT_PRICE ="price";

        //Quantity of the product
        public final static String COLUMN_PRODUCT_QUANTITY ="quantity";

        //Supplier name of the product
        public final static String COLUMN_SUPPLIER_NAME ="supplier";

        //Supplier phone number of the product
        public final static String COLUMN_SUPPLIER_PHONE ="phone";
    }
}
