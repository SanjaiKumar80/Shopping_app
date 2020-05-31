package com.example.shoppingapp.adapter;

import android.content.Context;
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
import com.example.shoppingapp.model.ListModel;
import com.example.shoppingapp.model.VerticalList;

import java.util.ArrayList;

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.VerticalViewHolder> /*implements Filterable*/ {
    private static final String TAG = VerticalListAdapter.class.getSimpleName();
    private ArrayList<VerticalList> verticalListArrayList;
    private ArrayList<ListModel> filteredList;
    private Context mContext;
    private ArrayList<ListModel> horizontals;
    private VerticalList VerticalList;
    private ListModel horizontalList;
    private boolean isLoaderVisible = false;

    public VerticalListAdapter(Context context, ArrayList<VerticalList> mList ) {
        this.mContext = context;
        this.verticalListArrayList = mList;
        this.filteredList = horizontals;

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
        VerticalList = verticalListArrayList.get(position);
        Log.d(TAG, "onBindViewHolderVerticalList: "+VerticalList);
        String title = VerticalList.getTitle();
        String scrollType = VerticalList.getScrollType();
        Log.d(TAG, "scrollType: "+scrollType);

        horizontals = VerticalList.getArrayList();
        Log.d(TAG, "horizontals: "+horizontals);
        holder.textView.setText(title);
        Log.d(TAG, "textViewtitle: "+title );
        ListItemAdapter ListItemAdapter  = new ListItemAdapter(mContext,horizontals);
        Log.d(TAG, "ListItemAdapter: "+ListItemAdapter);

        holder.recyclerView.setHasFixedSize(true);
        if (scrollType.equals("1")) {
            RecyclerView.LayoutManager layout = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layout);
            holder.recyclerView.setAdapter(ListItemAdapter);
//            holder.recyclerView.addItemDecoration(new SpacesItemDecoration(4));
        }else {
            RecyclerView.LayoutManager layout = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
            holder.recyclerView.setLayoutManager(layout);
            holder.recyclerView.setAdapter(ListItemAdapter);
//            holder.recyclerView.addItemDecoration(new SpacesItemDecoration(4));

        }

    }
    @Override
    public int getItemCount() {
        if (filteredList == null)
            return 0;
        return filteredList.size();
    }
    public void addItems(ArrayList<VerticalList> postItems) {
        Log.d(TAG, "addItems: "+postItems);
        verticalListArrayList.addAll(postItems);
//        singleItem.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
//                Log.d(TAG, "addLoading: "+singleItem.add(new ChildList()));
//        mList.add(new ParentList());

        notifyItemInserted(verticalListArrayList.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = filteredList.size() - 1;
        /*VerticalList item = getItem(position);
        if (item != null) {
            filteredList.remove(position);
            notifyItemRemoved(position);
        }*/
    }

    private ListModel getItem(int position){
        return horizontals.get(position);
    }

/*
    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d(TAG, "searchperformfiltering: ");
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredList = verticalListArrayList;
                } else {
                    ArrayList<VerticalList> filteredListItems = new ArrayList<>();
                    Log.d(TAG, "row.getTitle(): " + filteredList);


                    for (VerticalList row : filteredList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match
                        Log.d(TAG, "row.getTitle(): " + row.getTitle());

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFilteringrow: " + row);
                            filteredListItems.add(row);
                            Log.d(TAG, "performFilteringListItems: " + filteredListItems.toString());
                        }
                    }

                    filteredList = filteredListItems;
                }
                FilterResults filter = new FilterResults();
                filter.values = filteredList;
//                filter.count = filteredList.size();
                Log.d(TAG, "filterResults.values: " + filter.values.toString());
                return filter;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results)
            {
//                horizontals.clear();
                Log.d(TAG, "search publish "+ charSequence);
                filteredList = ((ArrayList<VerticalList>) results.values);
                Log.d(TAG, "publishResults: "+ (ArrayList<VerticalList>) results.values);
                notifyDataSetChanged();
            }

        };
    }
*/

/*`
    @Override
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
           */
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

    static class VerticalViewHolder extends RecyclerView.ViewHolder{
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
