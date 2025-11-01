package com.example.nexusstock;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecylerViewClass extends AppCompatActivity {
  RecyclerView recyclerView;
  ArrayList <String> id, name, category, size, quantity, price,productImage;
    ArrayList <String> img_uri;
    ImageView noItemFound;
   DB_Manager DB;
  CustomAdapter ad;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recyler_view);

        MainActivity main=new MainActivity();

        recyclerView=findViewById(R.id.recyclerView);
        DB=new DB_Manager(this,"InventoryDetails.db",null,2);
        id=new ArrayList<>();
        name=new ArrayList<>();
        category=new ArrayList<>();
        size=new ArrayList<>();
        quantity=new ArrayList<>();
        price=new ArrayList<>();
        img_uri=new ArrayList<>();

        noItemFound=findViewById(R.id.noItemFound);

        // Object of Custom Adapter to Show the Details
        ad=new CustomAdapter(RecylerViewClass.this,id, name, quantity, category, size, price, img_uri);
        recyclerView.setAdapter(ad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //It Filter the Category of Item (Shoe,Shirt,Pant) and show on their respective section
        String categoryFilter = getIntent().getStringExtra("CATEGORY_FILTER");

        if (categoryFilter != null && categoryFilter.equals("pant")) {
            displayPant();
        }else if(categoryFilter != null && categoryFilter.equals("shoe")){
            displayShoe();
        } else if (categoryFilter != null && categoryFilter.equals("shirt")) {
            displayShirt();
        }
    }
    //Method of Displaying Pants
    private void displayPant(){
        Cursor cursor= DB.getPants();
        if(cursor.getCount()==0){
            noItemFound.setImageResource(R.drawable.empty2);
        }else{
            while(cursor.moveToNext()){
                noItemFound.setVisibility(RecyclerView.GONE);
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                quantity.add(cursor.getString(2));
                category.add(cursor.getString(3));
                size.add(cursor.getString(4));
                price.add(cursor.getString(5));
                img_uri.add(cursor.getString(6));
            }
            cursor.close();
        }
    }
    //Method of Displaying Shoes
    private void displayShoe(){
        Cursor cursor= DB.getShoe();
        if(cursor.getCount()==0){
            noItemFound.setImageResource(R.drawable.empty2);
        }else{
            while(cursor.moveToNext()){
                noItemFound.setVisibility(RecyclerView.GONE);
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                quantity.add(cursor.getString(2));
                category.add(cursor.getString(3));
                size.add(cursor.getString(4));
                price.add(cursor.getString(5));
                img_uri.add(cursor.getString(6));
            }
            cursor.close();
        }
    }

    //Method of Displaying Shirts
    private void displayShirt(){
        Cursor cursor= DB.getShirts();
        if(cursor.getCount()==0){
            noItemFound.setImageResource(R.drawable.empty2);
        }else{
            while(cursor.moveToNext()){
                noItemFound.setVisibility(RecyclerView.GONE);
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                quantity.add(cursor.getString(2));
                category.add(cursor.getString(3));
                size.add(cursor.getString(4));
                price.add(cursor.getString(5));
                img_uri.add(cursor.getString(6));
            }
            cursor.close();
        }
    }
}