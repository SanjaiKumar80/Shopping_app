package com.example.shoppingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.ListItems;
import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ChildList;
import com.example.shoppingapp.model.ListModel;
import com.example.shoppingapp.products.ProductDetails;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private static ArrayList<ListModel> horizontalListItems;
    private static boolean isLoaderVisible = false;


    private Context context;

    ListItemAdapter(Context context, ArrayList<ListModel> horizontalListItems) {
        this.horizontalListItems = horizontalListItems;
        this.context = context;
    }

    @NotNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_model, viewGroup, false);
        return new ListItemAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ListItemAdapter.ViewHolder holder, int i) {
        Log.d("TAG", "onBindViewHolder: ");
        ListModel model = horizontalListItems.get(i);
        String modelProductId = model.getProductId();
        Log.d("TAG", "modelProductId: "+modelProductId);
        holder.itemView.setOnClickListener(v -> {
            Intent i1 = new Intent(context, ProductDetails.class);
            i1.putExtra("productitemsId",modelProductId);
            context.startActivity(i1);
        });

        holder.items_title.setText(model.getTitle());
        holder.items_name.setText(model.getName());
        holder.items_description.setText(model.getDescription());
        holder.items_brand.setText(model.getBrands());
//        holder.items_price.setText(model.getPrice());
        holder.items_price.setText(" \u20B9" + model.getPrice());
        Log.d("TAG", "items_price: "+holder.items_price);
//        Float number = Float.parseFloat(model.getStars());
        holder.items_stars.setRating(Float.parseFloat(model.getStars()));

        Picasso.with(context).load(model.getImage()).resize(200, 200).into(holder.items_image);
    }

    @Override
    public int getItemCount() {
        return horizontalListItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView items_title, items_brand, items_name, items_price, items_description;
        private RatingBar items_stars;
        private ImageView items_image;

        ViewHolder(View view) {
            super(view);

            items_title = view.findViewById(R.id.tv_list_items);
            items_image = view.findViewById(R.id.iv_list_items);
            items_title = view.findViewById(R.id.tv_list_title);
            items_image = view.findViewById(R.id.iv_list_items);
            items_brand = view.findViewById(R.id.tv_list_brand);
            items_name = view.findViewById(R.id.tv_list_name);
            items_price = view.findViewById(R.id.tv_list_price);
            items_description = view.findViewById(R.id.tv_list_description);
            items_stars = view.findViewById(R.id.rv_list_stars);
            items_image = view.findViewById(R.id.iv_list_image);
        }
    }
}
