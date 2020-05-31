package com.example.shoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ChildList;
import com.example.shoppingapp.model.ParentList;

import java.util.ArrayList;
import java.util.List;


public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.VerticalViewHolder> implements Filterable {
    private static final String TAG = ParentListAdapter.class.getSimpleName();
    private ArrayList<ParentList> mList;
    private List<ParentList> filteredList;
    private Context mContext;
    private ArrayList<ChildList> singleItem;
    private ParentList ParentList;
    private boolean isLoaderVisible = false;
    SearchFilterAdapter filterAdapter;

    public ParentListAdapter(Context context, ArrayList<ParentList> mList) {
        this.mContext = context;
        this.mList = mList;
        this.filteredList = mList;
        filterAdapter = new SearchFilterAdapter(mList, this);

    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list_item_vertical, parent, false);
        return new VerticalViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolderVertical: ");
        ParentList = filteredList.get(position);
        Log.d(TAG, "onBindViewHolderParentList: " + filteredList);
        String title = ParentList.getTitle();
        String scrollType = ParentList.getScrollType();
        Log.d(TAG, "scrollType: " + scrollType);

        singleItem = ParentList.getArrayList();
        holder.textView.setText(title);
        Log.d(TAG, "textViewtitle: " + title);
        ChildListAdapter ChildListAdapter = new ChildListAdapter(mContext, singleItem);
        holder.recyclerView.setHasFixedSize(true);
        if (scrollType.equals("1")) {
            RecyclerView.LayoutManager layout = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layout);
            holder.recyclerView.setAdapter(ChildListAdapter);
//            holder.recyclerView.addItemDecoration(new SpacesItemDecoration(4));
        } else {
            RecyclerView.LayoutManager layout = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
            holder.recyclerView.setLayoutManager(layout);
            holder.recyclerView.setAdapter(ChildListAdapter);
//            holder.recyclerView.addItemDecoration(new SpacesItemDecoration(4));

        }

    }
    @Override
    public int getItemCount() {
        if (filteredList == null)
            return 0;
        return filteredList.size();
    }

    public void addItems(ArrayList<ParentList> postItems) {
        Log.d(TAG, "addItems: " + postItems);
        mList.addAll(postItems);
//        singleItem.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
//                Log.d(TAG, "addLoading: "+singleItem.add(new ChildList()));
//        mList.add(new ParentList());

        notifyItemInserted(mList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mList.size() - 1;
        ParentList item = getItem(position);
        if (item != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        singleItem.clear();
        notifyDataSetChanged();
    }

    private ParentList getItem(int position) {
        return filteredList.get(position);
    }

    public void setList(ArrayList<ParentList> list) {
        this.mList = list;
    }

    public void filterList(String text) {
        filterAdapter.filter(text);
    }


    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d(TAG, "performFiltering: ");
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredList = mList;
                }
                else {
                    ArrayList<ParentList> filteredListItems = new ArrayList<>();
                    for (ParentList row : filteredList) {
                        Log.d(TAG, "performFilteringfilteredList: "+filteredList);

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString)) {
                            filteredListItems.add(row);
                            Log.d(TAG, "performFilteringrow: "+row);
                            Log.d(TAG, "filteredListItems: "+filteredListItems.toString());


                        }
                    }
                    filteredList = filteredListItems;
                }
                FilterResults filter = new FilterResults();
                filter.values = filteredList;
//                filter.count = filteredList.size();
                Log.d(TAG, "filterResults.values: "+filter.values);
                return filter;

            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results)
            {

                Log.d(TAG, "search publish "+ charSequence);
                filteredList = (List<ParentList>) results.values;
                Log.d(TAG, "publishResults: "+results.values);
                notifyDataSetChanged();

            }


        };
    }


 /*   @Override
    public Filter getFilter() {
        return searchFilter ;
    }
    private Filter searchFilter = new Filter() {

//        ArrayList<ChildList> filteredList = ParentList.getArrayList();
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();


            if (charString.isEmpty()) {
//                filteredList.addAll(singleItem);
            } else {
                ArrayList<ChildList> filteredList = ParentList.getArrayList();
                Log.d(TAG, "filteredList: "+filteredList);
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d(TAG, "performFiltering: "+filterPattern);
                for (ChildList item : singleItem) {
                    Log.d(TAG, "ChildList item: "+ item.toString());
                    if (item.getListItemsTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                        Log.d(TAG, "filteredList.add(item) "+filteredList.add(item));
                    }
                }
singleItem = filteredList;
            }

            FilterResults results = new FilterResults();
            Log.d(TAG, "FilterResults: "+results);
/* Log.d(TAG, "FilterResultsfilteredList: "+filteredList);
            results.values = filteredList;*//*

            return results;



        }



        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            singleItem.clear();
            Log.d(TAG, "search publish "+ constraint);
            singleItem = (ArrayList<ChildList>) results.values;
            Log.d(TAG, "publishResults: "+singleItem);

//            singleItem.addAll((List) results.values);
//          notifyDataSetChanged();

        }

    };
*/


    static class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
//        Button  btnMore;

        VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view1);
            recyclerView.setHasFixedSize(true);


            textView = itemView.findViewById(R.id.tv_title);
//             btnMore = itemView.findViewById(R.id.btn_more);


        }
    }
}


