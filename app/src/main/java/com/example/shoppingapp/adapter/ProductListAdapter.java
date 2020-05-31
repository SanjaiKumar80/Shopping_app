package com.example.shoppingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Products> productsList;
     private Context mContext;


    public ProductListAdapter(List<Products> productsList, Context mContext) {
        this.productsList = productsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_list_item, parent, false);
        return new ViewHolder(v1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int i) {
        Log.d("TAG", "productListAdapter: ");
        holder.product_name.setText(productsList.get(i).getName());
        holder.product_description.setText(productsList.get(i).getDescription());
        Picasso.with(mContext).load(productsList.get(i).getImage_url()).into(holder.product_image);
        holder.product_discountPrice.setText(" \u20B9" + productsList.get(i).getDiscountPrice());
        String productprice = " MRP : \u20B9" + productsList.get(i).getMrpPrice();
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(productprice);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spanBuilder.setSpan(
                strikethroughSpan, // Span to add
                7,// Start of the span
                spanBuilder.length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Text changes will not reflect in the strike changing
        );
        holder.product_price.setText(spanBuilder);
        holder.product_save.setText(" Save : \u20B9" + productsList.get(i).getSave());
        holder.product_stars.setRating(Float.parseFloat(productsList.get(i).getStars()));
        Log.d("TAG", "holder.product_stars:"+holder.product_stars);


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_name, product_description, product_price, product_discountPrice, product_save;
        private ImageView product_image;
        private RatingBar product_stars;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name =  itemView.findViewById(R.id.tv_product_name);
            product_description = itemView.findViewById(R.id.tv_product_description);
            product_image = itemView.findViewById(R.id.iv_product_image);
            product_price = itemView.findViewById(R.id.tv_product_price);
            product_discountPrice = itemView.findViewById(R.id.tv_product_discount_price);
            product_save = itemView.findViewById(R.id.tv_product_save);
            product_stars = itemView.findViewById(R.id.rb_product_stars);

        }
    }
}
