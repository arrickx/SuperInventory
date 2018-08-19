package com.example.android.superinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.superinventory.data.InventoryContract.InventoryEntry;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of inventory data in the {@link Cursor}.
 */
public class InventoryCursorAdapter extends CursorAdapter{

    //Bind view with Butter Knife for cleaner code.
    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.price) TextView priceTextView;
    @BindView(R.id.quantity) TextView quantityTextView;
    @BindView(R.id.sale) Button saleButton;
    @BindString(R.string.out_of_stock) String outOfStock;
    @BindString(R.string.dollar_sign) String dollar;
    @BindString(R.string.current_quantity) String currentQuantity;
    @BindString(R.string.sale_error) String saleError;
    @BindString(R.string.sale_success) String saleSuccess;

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current inventory can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ButterKnife.bind(this,view);

        // Find the important attributes
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the attributes from the Cursor for the current inventory
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = dollar + cursor.getString(priceColumnIndex);
        String productQuantity = currentQuantity + cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current inventory
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);

        // If quantity is zero, instead of displaying 0, will display out of stock.
        if(cursor.getInt(quantityColumnIndex)!=0) {
            quantityTextView.setText(productQuantity);
        } else {
            quantityTextView.setText(outOfStock);
        }

        // Get the current position in the cursor
        final int position = cursor.getPosition();

        // Set on click listener for the sale button
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to cursor current position
                cursor.moveToPosition(position);

                // Read the current quantity
                int quantity = cursor.getInt(quantityColumnIndex);

                // if quantity less than 1, won't process anything
                // otherwise will minus the inventory by 1.
                if (quantity<1) {
                    // Show a message when out of stock
                    Toast.makeText(view.getContext(), saleError, Toast.LENGTH_SHORT).show();
                } else {
                    // Create ContentValue fo sale button
                    ContentValues values = new ContentValues();

                    // Get the current product ID
                    int id = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));

                    // Set up the uri in the current position
                    Uri currentUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                    // Minus the quantity by 1 when an item sold
                    quantity -= 1;

                    // Update the quantity in the database
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    context.getContentResolver().update(currentUri, values, null, null);

                    // Show a message when it sale
                    Toast.makeText(view.getContext(), saleSuccess, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
