package com.example.nexusstock;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateItem extends AppCompatActivity {

    Button takeID, Update_Item;
    EditText updateID, updateQuantity, updateCategory, updateSize, updatePrice;
    DB_Manager dbManager;
    TextView heading;
    int currentItemId = -1; //Set Current Item ID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        takeID = findViewById(R.id.takeID);
        Update_Item = findViewById(R.id.Update_Item);
        updateID = findViewById(R.id.updateID);
        updateQuantity = findViewById(R.id.updateQuantity);
        updateCategory = findViewById(R.id.updateCategory);
        updateSize = findViewById(R.id.updateSize);
        updatePrice = findViewById(R.id.updatePrice);
        heading=findViewById(R.id.heading);

        setFieldsEnabled(false); //Fields of Update is Disabled
        Update_Item.setEnabled(false);

        dbManager = new DB_Manager(this, "InventoryDetails.db", null, 2);

        takeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndDisplayItemDetails(); //Method to Fetch and Display Data on their Respective Fields to Update

            }
        });
        Update_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdate();  // Method to Perform Updates in the Table when clicked on update Iten
            }
        });
    }
    // This Method Unable the Field for Edit if Id found in the Database
    private void setFieldsEnabled(boolean enabled) {
        updateQuantity.setEnabled(enabled);
        updateCategory.setEnabled(enabled);
        updateSize.setEnabled(enabled);
        updatePrice.setEnabled(enabled);
    }
    //This Method to Fetch and Display Data on their Respective Fields to Update
    private void fetchAndDisplayItemDetails() {
        String idText = updateID.getText().toString().trim(); // Read ID

        if (idText.isEmpty()) {
            Toast.makeText(this, "Please enter an ID to view details.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            Cursor cursor = dbManager.getItemDetails(id);// ID Found in the Database
            heading.setText(("Update Details of ID: "+ id));
            if(!cursor.moveToNext()){  //If ID not Found in the Database
                heading.setText(" ");
            }
            //If Id Found in the DB
            if (cursor != null && cursor.moveToFirst()) {

                currentItemId = id; //ID Equals
                //Getting Data of Each Column of the ID from The Database
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                // Set that Data in the EditText for Edit
                updateQuantity.setText(String.valueOf(quantity));
                updateCategory.setText(category);
                updateSize.setText(size);
                updatePrice.setText(String.valueOf(price));

                setFieldsEnabled(true);  //Now Fields Enable
                Update_Item.setEnabled(true); //Update_Item Button Enable
                updateID.setEnabled(false); //Update ID Button Disable

                Toast.makeText(this, "Details loaded for ID: " + id, Toast.LENGTH_SHORT).show();
            //ID not Found in the Database
            } else {
                Toast.makeText(this, "Item with ID " + id + " not found.", Toast.LENGTH_LONG).show();
                setFieldsEnabled(false);
                Update_Item.setEnabled(false);
                updateID.setEnabled(true);

                //Field Remain Empty
                updateQuantity.setText("");
                updateCategory.setText("");
                updateSize.setText("");
                updatePrice.setText("");
                currentItemId = -1;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (NumberFormatException e) {
            heading.setText(" ");
            Toast.makeText(this, "Invalid ID format. Please enter a number.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    //This Method Perform Updates in the Table of That Particular ID
    private void performUpdate() {
        if (currentItemId == -1) {
            Toast.makeText(this, "Please load an item's details first.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int newQuantity = Integer.parseInt(updateQuantity.getText().toString()); //Read the New Updated Data
            String newCategory = updateCategory.getText().toString();                //Read the New Updated Data
            String newSize = updateSize.getText().toString();                        //Read the New Updated Data
            double newPrice = Double.parseDouble(updatePrice.getText().toString());  //Read the New Updated Data
            //Update The Data
            int rowsAffected = dbManager.updateQuantity(
                    currentItemId,
                    newQuantity,
                    newCategory,
                    newSize,
                    newPrice
            );
            // If Update Successfully
            if (rowsAffected > 0) {
                showSimpleDialog(); //Show Dialog Box

            } else {
                Toast.makeText(this, "Update failed (0 rows affected).", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity and Price must be valid numbers.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred during update: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Dailog Box to Show the Message "Updated Successfully"
    private void showSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Updated Successfully!");
        builder.setMessage("Product No " + currentItemId + " updated successfully!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
