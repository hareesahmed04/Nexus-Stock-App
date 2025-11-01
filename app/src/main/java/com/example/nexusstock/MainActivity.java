package com.example.nexusstock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    ImageView shoes , shirt ,pant , addItem, deleteItem, updateItem;
    Switch mode;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})

    // Checking Permission for Notification (Granted or Not)
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notification Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notification Permission is not Granted", Toast.LENGTH_SHORT).show();
                }
            });

      // Toolbar for Label in Main Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItem=findViewById(R.id.addItem);
        deleteItem=findViewById(R.id.deleteItem);
        shoes=findViewById(R.id.shoe);
        shirt=findViewById(R.id.shirt);
        pant=findViewById(R.id.pant);
        updateItem=findViewById(R.id.updateItem);
        mode=findViewById(R.id.mode);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RecylerViewClass.class);
                intent.putExtra("CATEGORY_FILTER", "shirt");
                startActivity(intent);
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddData.class);
                startActivity(intent);
            }
        });
        pant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RecylerViewClass.class);
                intent.putExtra("CATEGORY_FILTER", "pant");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RecylerViewClass.class);
                intent.putExtra("CATEGORY_FILTER", "shoe");
                startActivity(intent);
            }
        });
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DeleteItem.class);
                startActivity(intent);
            }
        });
        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, UpdateItem.class);
                startActivity(intent);
            }
        });

        // This code Changing the Night/Light Mode
        int currentNightMode= AppCompatDelegate.getDefaultNightMode();
        mode.setChecked(currentNightMode== AppCompatDelegate.MODE_NIGHT_YES);

        mode.setOnCheckedChangeListener((buttonView,isChecked) -> {
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();

        });
        requestNotificationPermission();
    }

    // Requesting Permission for Notification
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    }

