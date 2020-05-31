package com.example.shoppingapp.adapter;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shoppingapp.model.ParentList;

import java.util.ArrayList;


public class SearchFilterAdapter extends Filter {
    private static final String TAG = SearchFilterAdapter.class.getSimpleName();
  private ParentListAdapter adapter;
  private ArrayList<ParentList> arrayList;
  private ArrayList<ParentList> filteredList;

    private ParentList ParentList;



    SearchFilterAdapter(ArrayList<ParentList> arrayList, ParentListAdapter parentListAdapter) {
        this.arrayList = arrayList;
        this.adapter = parentListAdapter;
        this.filteredList = new ArrayList<>();
    }


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = arrayList;
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
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results)
            {

                Log.d(TAG, "search publish "+ charSequence);
                filteredList = (ArrayList<ParentList>) results.values;
                Log.d(TAG, "publishResults: "+filteredList.toString());
               adapter.notifyDataSetChanged();

            }




          /*  @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                filteredList.clear();
                String charString = charSequence.toString();


                if (charString.isEmpty()) {
                    filteredList = arrayList;
                } else {
                    Log.d(TAG, "performFiltering: " + filteredList);

                    ArrayList<ParentList> filteredListItems = new ArrayList<>();
                    for (ParentList row : filteredList) {
                        Log.d(TAG, "performFilteringfilteredList: ");
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString)) {
//                            filteredListItems.add(row);
                            Log.d(TAG, "filteredListItems: " + filteredListItems.toString());

                        }
                    }
                    filteredList = filteredListItems;

                }
                final FilterResults results = new FilterResults();

                results.values = filteredList;
//                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d(TAG, "publishResults: " + adapter);
                filteredList = (ArrayList<ParentList>) results.values;
                adapter.notifyDataSetChanged();

            }*/


/*
    @NonNull
    @Override
    public SearchFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHoldersearchviewAdapter: ");
        ParentList = mList.get(position);
        String title = ParentList.getTitle();
        Log.d(TAG, "ParentListsearch: "+title);
        mArrayList = ParentList.getArrayList();
//        viewHolder.textView.setText(title);
        ChildListAdapter ChildListAdapter  = new ChildListAdapter(mContext,mArrayList);
//        viewHolder.recyclerView.setHasFixedSize(true);



    }

    @Override
    public int getItemCount() {
        if (filteredList == null)
            return 0;
        return filteredList.size();
    }
*/

/*
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
                    filteredList.addAll(mArrayList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ChildList item : mArrayList) {
                        Log.d(TAG, "ChildList item: "+ item);
                        if (item.getListItemsTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                    mArrayList = filteredList;
                }
                FilterResults results = new FilterResults();
                Log.d(TAG, "FilterResults: "+results);
                Log.d(TAG, "FilterResultsfilteredList: "+filteredList);
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d(TAG, "search publish "+ constraint);
                mArrayList = (ArrayList<ChildList>) results.values;
                Log.d(TAG, "publishResults: "+mArrayList);

//            mArrayList.addAll((List) results.values);
                notifyDataSetChanged();


            }
        };
    }
*/



/*
    static class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView textView;
//        Button  btnMore;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view1);
//            recyclerView.setHasFixedSize(true);


            textView = itemView.findViewById(R.id.tv_title);
//             btnMore = itemView.findViewById(R.id.btn_more);


        }
    }
*/

}

