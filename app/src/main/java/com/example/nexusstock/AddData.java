package com.example.nexusstock;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddData extends AppCompatActivity {
EditText editID,editName,editSize,editCategory,editQuantity,editPrice;
ImageButton itemImage;
Button insertItem;
    private Uri selectedImageUri = null; //Image is null / not selected
DB_Manager DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_data);

        DB=new DB_Manager(this,"InventoryDetails.db",null,2);

        editID=findViewById(R.id.editID);
        itemImage=findViewById(R.id.itemImage);
        editName=findViewById(R.id.editName);
        editSize=findViewById(R.id.editSize);
        editCategory=findViewById(R.id.editCategory);
        editQuantity=findViewById(R.id.editQuantity);
        editPrice=findViewById(R.id.editPrice);
        insertItem=findViewById(R.id.insertItem);

        // This Code Open the new Window of Document Folder From where we can select Images
        final ActivityResultLauncher<String[]> openDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        Log.d("ImagePicker", "Selected URI: " + uri);
                        selectedImageUri = uri;
                        itemImage.setImageURI(uri);

                        // This try{} take permission
                        try {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Toast.makeText(this, "Image permission granted successfully.", Toast.LENGTH_SHORT).show();
                        } catch (SecurityException e) {
                            Log.e("ImagePicker", "Failed to take persistent URI permission", e);
                            Toast.makeText(this, "Failed to get image access permission. Check URI source.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("ImagePicker", "No media selected");
                    }
                }
        );

        itemImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                   // Now calling the new launcher with the correct MIME type for images
                   openDocumentLauncher.launch(new String[]{"image/*"});
               }
           });
        insertItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_txt= editID.getText().toString();
                String name_txt=editName.getText().toString();
                String size_txt= editSize.getText().toString();
                String category_txt=editCategory.getText().toString();
                String quantity_txt=editQuantity.getText().toString();
                String price_txt=editPrice.getText().toString();

               if(id_txt.isEmpty() || name_txt.isEmpty() || size_txt.isEmpty() || category_txt.isEmpty() || quantity_txt.isEmpty() || price_txt.isEmpty()) {
                   Toast.makeText(AddData.this, "Please Fill All Details!", Toast.LENGTH_SHORT).show();
                   return;
                }
               int id;
               int quantity;
               double price;

               try{
                    id=Integer.parseInt(id_txt);
                    quantity=Integer.parseInt(quantity_txt);
                    price=Double.parseDouble(price_txt);
               } catch (NumberFormatException e) {
                   Toast.makeText(AddData.this, "Please Enter Valid Values!", Toast.LENGTH_SHORT).show();
                   return;
               }
               //This if () select The Image and insert into the Database
                if (selectedImageUri == null) {
                    Toast.makeText(AddData.this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String image_uri_string = selectedImageUri.toString();

                Boolean checkInsertData=DB.insertUserData(id ,name_txt,quantity,category_txt,size_txt, price, image_uri_string);
                if(checkInsertData==true){
                    showSimpleDialogAdd();
                }else {
                    Toast.makeText(AddData.this, "New Entry not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

        //This is the Dialog Box Method
    private void showSimpleDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Item Added");
        builder.setMessage("New Product Added in the Inventory");

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