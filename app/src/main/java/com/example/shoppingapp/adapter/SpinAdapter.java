package com.example.shoppingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.shoppingapp.model.SpinnerModel;
import com.example.shoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.shoppingapp.login.RegisterActivity.selectedItem;

public class SpinAdapter extends ArrayAdapter<SpinnerModel> {
    private Context context;
    private ArrayList<SpinnerModel> values;


    public SpinAdapter(@NonNull Context context, int resource, ArrayList<SpinnerModel> values) {
        super(context, resource);
        this.context = context;
        this.values = values;


    }
    public String getCountryId(int position){
        return values.get(position).getCountryId();
    }
    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public SpinnerModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @SuppressLint("SetTextI18n")
    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText("+"  + values.get(position).getCountrycode());



        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NotNull ViewGroup parent) {

        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        if (position == selectedItem) {
            label.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
        }
        else {
            // for other views
            label.setBackgroundColor(context.getResources().getColor(R.color.white));

        }
        label.setTextColor(Color.BLACK);
        label.setText(String.format("+%s %s", values.get(position).getCountrycode(), values.get(position).getCountryName()));
        label.setGravity(Gravity.CENTER_VERTICAL);


        return label;
    }
}
