package com.example.nexusstock;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class DB_Manager extends SQLiteOpenHelper {

    MainActivity ma=new MainActivity();


    private static final String TABLE_INVENTORY = "Inventory";
    private static final String CHANNEL_ID = "low_stock_channel";
    private static final int NOTIFICATION_ID = 1;// This is Notification ID
    private Context context; // context of this DB class

    public DB_Manager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "InventoryDetails.db", null, 2);
        this.context = context;
        createNotificationChannel();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE Inventory(id INTEGER PRIMARY KEY  , name TEXT, quantity INTEGER,category TEXT, size TEXT , price DOUBLE, image TEXT)";
        db.execSQL(create);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(drop, new String[]{"Inventory"});
        onCreate(db);
    }

    // This insertUserData use to insert the data into the table
    Boolean insertUserData(int id, String name, int quantity, String category, String size, Double price, String imageUriString) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("name",name);
        values.put("quantity",quantity);
        values.put("category",category);
        values.put("size",size);
        values.put("price",price);
        values.put("image", imageUriString);

        long result = db.insert("Inventory", null, values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getdata(){
        SQLiteDatabase DB=getWritableDatabase();
        Cursor cursor =DB.rawQuery("Select * from Inventory",null);
        return cursor;
    }
    public Cursor getShirts() {
        SQLiteDatabase DB = getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Inventory WHERE category = 'shirt'", null);
        return cursor;
    }
    public Cursor getPants() {
        SQLiteDatabase DB = getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Inventory WHERE category ='pant'", null);
        return cursor;
    }
    public Cursor getShoe() {
        SQLiteDatabase DB = getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Inventory WHERE category = 'shoe'", null);
        return cursor;
    }
    //This Method Update the Item in the Table
    public int updateQuantity(int id, int quantity, String category, String size, double price) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = 0;// Initial row is 0
        String name = "";

        Cursor cursor = getItemDetails(id);  // Item Fetch and Update by ID
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
       //These are the Values we want to update in the table of any Item
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        values.put("category", category);
        values.put("size", size);
        values.put("price", price);
       //This code actually perform update
        rowsAffected = db.update(
                TABLE_INVENTORY,
                values,
                "id = ?", // WHERE clause ( Where ID)
                new String[]{String.valueOf(id)}
        );
        db.close();
        //This if() check the Quantity of item after Update it must bot be less than 5
        if (rowsAffected > 0 && quantity < 5) {
            checkLowQuantity(id, name, quantity);
        }
        return rowsAffected;
    }
    //This Method is for deleting the Item
    public int deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = 0;

        rowsDeleted = db.delete(
                TABLE_INVENTORY,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
        return rowsDeleted;
    }
    public Cursor getItemDetails(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_INVENTORY,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        return cursor;
    }
                         // Give Notification when Quantity of any item Remain less than 5
                         // Create Notification Channel
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Low Stock Notification";
            String description = "Notifications for inventory items with low stock";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.notification1);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);

            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //This Method Show Notification when Quantity of any item is less than 5
    public void checkLowQuantity(int id,String name, int quantity) {
        if (context == null)
            return;

        Intent notifyIntent = new Intent(context, UpdateItem.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)

                .setSmallIcon(R.drawable.empty2) // Replace with your notification icon
                .setContentTitle("Low Stock Alert!")
                .setContentText("Quantity for " + name + " (ID: " + id + ") is only " + quantity + ". Reorder now!)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Dismisses the notification when clicked

        NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
        builder.setContentIntent(notifyPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    // This method Compress Image inti Bytes Array
    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Or JPEG, WEBP
        return stream.toByteArray();
    }
    }












