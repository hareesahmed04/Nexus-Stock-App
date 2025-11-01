package com.example.nexusstock;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private ArrayList id_id;
    private ArrayList name_id;
    private ArrayList quantity_id;
    private ArrayList category_id;
    private ArrayList size_id;
    private ArrayList price_id;
    private ArrayList<String> img_id;

      //Constructor of Custom Adapter
    public CustomAdapter(Context context, ArrayList id_id, ArrayList name_id, ArrayList quantity_id, ArrayList category_id, ArrayList size_id, ArrayList price_id, ArrayList img_id) {
        this.context = context;
        this.id_id = id_id;
        this.name_id = name_id;
        this.quantity_id = quantity_id;
        this.category_id = category_id;
        this.size_id = size_id;
        this.price_id = price_id;
        this.img_id=img_id;
    }
    //This Hold The Custom Adpapter of custom_adapter
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_adapter,parent,false);
        return new ViewHolder(v);
    }
    //This Method Bind the Values of each Column Corresponding to the TextView . (Like : Id: 1001)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_id.setText(String.valueOf(id_id.get(position)));
        holder.name_id.setText(String.valueOf(name_id.get(position)));
        holder.category_id.setText(String.valueOf(category_id.get(position)));
        holder.size_id.setText(String.valueOf(size_id.get(position)));
        holder.quantity_id.setText(String.valueOf(quantity_id.get(position)));
        holder.price_id.setText(String.valueOf(price_id.get(position)));

        String imageUriString = img_id.get(position);
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            holder.itemImageView.setImageURI(imageUri);
        }
    }
    @Override
    public int getItemCount() {
        return id_id.size();
    }

    //This class show the Item Details in the Recycler view in the form of Custom Adapter
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_id,name_id,quantity_id,category_id,size_id,price_id;
        ShapeableImageView itemImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_id=itemView.findViewById(R.id.textID);
            name_id=itemView.findViewById(R.id.textName);
            quantity_id=itemView.findViewById(R.id.textQuantity);
            category_id=itemView.findViewById(R.id.textCategory);
            size_id=itemView.findViewById(R.id.textSize);
            price_id=itemView.findViewById(R.id.textPrice);
            itemImageView=itemView.findViewById(R.id.itemImageView);
        }
    }
    }
