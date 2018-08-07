package com.example.android.superinventory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

//Create new inventory or edit the existing inventory
public class EditorActivity extends AppCompatActivity {

    //EditText field for product name
    private EditText mProductNameEditText;

    //EditText field for product price
    private EditText mProductPriceEditText;

    //EditText field for product quantity
    private EditText mProductQuantityEditText;

    //EditText field for supplier name
    private EditText mSupplierNameEditText;

    //EditText field for supplier phone number
    private EditText mSupplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEditText = findViewById(R.id.edit_product_name);
        mProductPriceEditText = findViewById(R.id.edit_product_price);
        mProductQuantityEditText = findViewById(R.id.edit_product_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
    }
}