package com.example.nexusstock;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {
    private Context context;
    // The data lists used for display (the filtered list)
    private ArrayList id_id;
    private ArrayList name_id;
    private ArrayList quantity_id;
    private ArrayList category_id;
    private ArrayList size_id;
    private ArrayList price_id;
    private ArrayList<String> img_id;

    // The original data lists (full list before filtering)
    private ArrayList<String> original_id_id;
    private ArrayList<String> original_name_id;
    private ArrayList<String> original_quantity_id;
    private ArrayList<String> original_category_id;
    private ArrayList<String> original_size_id;
    private ArrayList<String> original_price_id;
    private ArrayList<String> original_img_id;

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

        this.original_id_id = new ArrayList<>((ArrayList<String>) id_id);
        this.original_name_id = new ArrayList<>((ArrayList<String>) name_id);
        this.original_quantity_id = new ArrayList<>((ArrayList<String>) quantity_id);
        this.original_category_id = new ArrayList<>((ArrayList<String>) category_id);
        this.original_size_id = new ArrayList<>((ArrayList<String>) size_id);
        this.original_price_id = new ArrayList<>((ArrayList<String>) price_id);
        this.original_img_id = new ArrayList<>((ArrayList<String>) img_id);

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

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
    private Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> filteredNames = new ArrayList<>();
            ArrayList<String> filteredIds = new ArrayList<>();
            ArrayList<String> filteredQuantities = new ArrayList<>();
            ArrayList<String> filteredCategories = new ArrayList<>();
            ArrayList<String> filteredSizes = new ArrayList<>();
            ArrayList<String> filteredPrices = new ArrayList<>();
            ArrayList<String> filteredImgUris = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No search term, show the original full list
                filteredNames.addAll(original_name_id);
                filteredIds.addAll(original_id_id);
                filteredQuantities.addAll(original_quantity_id);
                filteredCategories.addAll(original_category_id);
                filteredSizes.addAll(original_size_id);
                filteredPrices.addAll(original_price_id);
                filteredImgUris.addAll(original_img_id);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Loop through the original names to find matches
                for (int i = 0; i < original_name_id.size(); i++) {
                    String name = original_name_id.get(i);
                    if (name.toLowerCase().contains(filterPattern)) {
                        // Found a match, add all corresponding item details
                        filteredIds.add(original_id_id.get(i));
                        filteredNames.add(original_name_id.get(i));
                        filteredQuantities.add(original_quantity_id.get(i));
                        filteredCategories.add(original_category_id.get(i));
                        filteredSizes.add(original_size_id.get(i));
                        filteredPrices.add(original_price_id.get(i));
                        filteredImgUris.add(original_img_id.get(i));
                    }
                }
            }
            // Group all filtered lists into a temporary ArrayList of ArrayLists
            ArrayList<ArrayList<String>> filteredData = new ArrayList<>();
            filteredData.add(filteredIds);
            filteredData.add(filteredNames);
            filteredData.add(filteredQuantities);
            filteredData.add(filteredCategories);
            filteredData.add(filteredSizes);
            filteredData.add(filteredPrices);
            filteredData.add(filteredImgUris);

            results.values = filteredData;
            results.count = filteredNames.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Clear the currently displayed lists
            id_id.clear();
            name_id.clear();
            quantity_id.clear();
            category_id.clear();
            size_id.clear();
            price_id.clear();
            img_id.clear();

            // Cast and retrieve the filtered data
            ArrayList<ArrayList<String>> filteredData = (ArrayList<ArrayList<String>>) results.values;

            if (filteredData != null && filteredData.size() == 7) {
                id_id.addAll(filteredData.get(0));
                name_id.addAll(filteredData.get(1));
                quantity_id.addAll(filteredData.get(2));
                category_id.addAll(filteredData.get(3));
                size_id.addAll(filteredData.get(4));
                price_id.addAll(filteredData.get(5));
                img_id.addAll(filteredData.get(6));
            }
            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
        }
    };

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
