package com.example.nexusstock;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeleteItem extends AppCompatActivity {

    EditText delete_text;
    Button delete;
    DB_Manager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_item);

        db=new DB_Manager(this,"InventoryDetails.db",null,2);

        delete_text = findViewById(R.id.delete_text);
        delete = findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String del=delete_text.getText().toString();
                if (del.isEmpty()) {
                    Toast.makeText(DeleteItem.this, "Please enter an ID to delete.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int itemId = Integer.parseInt(del);
                    int rowsDeleted = db.deleteItem(itemId);


                    // Show Dialog Box
                    if (rowsDeleted > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteItem.this);

                        builder.setTitle("Item Deleted");
                        builder.setMessage("Item with ID " +itemId+" deleted successfully.");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        delete_text.setText("");

                    } else {
                        Toast.makeText(DeleteItem.this, "Error: Item with ID " + itemId + " not found.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(DeleteItem.this, "Invalid ID format. Please enter a number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    }
