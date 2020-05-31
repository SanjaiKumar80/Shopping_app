package com.example.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.ListItems;
import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ChildList;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ViewHolder>  {
    private static  final String TAG = ChildListAdapter.class.getSimpleName();
    private  ArrayList<ChildList> ChildListItems;
//    private static boolean isLoaderVisible = false;
    private Context context;

    ChildListAdapter(Context context, ArrayList<ChildList> ChildListItems) {
        this.ChildListItems = ChildListItems;
        this.context = context;
    }

    @NotNull
    @Override
    public ChildListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildListAdapter.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: ");
        ChildList ChildList = ChildListItems.get(i);


        viewHolder.items_title.setText(ChildList.getListItemsTitle());
        Log.d(TAG, "onBindViewHoldertext: "+ChildList.getListItemsTitle());
        Log.d(TAG, "onBindViewHolderid: "+ChildList.getProductid());
        Picasso.with(context).load(ChildList.getImgUrl()).resize(200, 200).into(viewHolder.items_image);
        Log.d(TAG, "onBindViewHolderimage: "+ ChildList.getImgUrl());
        viewHolder.itemView.setOnClickListener(v -> {
            Intent i1 = new Intent(context, ListItems.class);
            i1.putExtra("product_id",ChildList.getProductid());
            Log.d(TAG, "productListid"+ChildList.getProductid());
            context.startActivity(i1);
        });


    }

    @Override
    public int getItemCount() {
        return ChildListItems.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView items_title;
        private ImageView items_image;
        ViewHolder(View view) {
            super(view);

            items_title = view.findViewById(R.id.tv_list_items);
            items_image =  view.findViewById(R.id.iv_list_items);
        }

    }




    // for both short and long click
}
