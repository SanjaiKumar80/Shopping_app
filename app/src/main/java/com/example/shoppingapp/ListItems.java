package com.example.shoppingapp;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shoppingapp.adapter.VerticalListAdapter;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.model.ListModel;
import com.example.shoppingapp.model.VerticalList;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import navigationDrawer.NavigationActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.shoppingapp.apiUrl.ApiClient.API_ITEMS;

public class ListItems extends AppCompatActivity {
    private static final String TAG = ListItems.class.getSimpleName();
    VerticalListAdapter adapter;
    ArrayList<VerticalList> listArrayList;
    String productid;
    SearchView searchView;
    ArrayList<ListModel> horizontalList;
    Toolbar toolbar;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        listArrayList = new ArrayList<>();
        productid = getIntent().getStringExtra("product_id");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerview);
        Listitems();
    }
    private void Listitems() {
        Log.d(TAG, "ListItems: ");
        Log.d(TAG, "productid: "+productid);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ITEMS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.listItems(productid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    final String java = response.body().string();
                    Log.d(TAG, "onResponsesend: " + java);
                    JSONObject object = new JSONObject(java);

                    JSONArray array = object.getJSONArray("category");
                    Log.d(TAG, "onResponsearray: " + array);
                    listArrayList = new ArrayList<>();


                    for (int i = 0; i < array.length(); i++) {
                        VerticalList list = new VerticalList();
                        JSONObject c = (JSONObject) array.get(i);

                        String title = c.getString("title");
                        Log.d(TAG, "onResponsename: " + title);

                        String scrolltype = c.getString("scrolltype");
                        Log.d(TAG, "onResponsescrolltype: " + scrolltype);

                        list.setTitle(title);
                        list.setScrollType(scrolltype);
                        JSONArray object1 = c.getJSONArray("product_id");
                        for (int j = 0; j < object1.length(); j++) {
                            JSONObject object2 = object1.getJSONObject(j);
                            String listId = object2.getString("list_id");
                            if (listId.equals(productid)) {
                                Log.d(TAG, "onResponselistId: " + listId);
                                Log.d(TAG, "onResponseproductid: " + productid);

                                JSONArray jsonArray1 = object2.getJSONArray("product");

                                horizontalList = new ArrayList<>();
                                for (int k = 0; k < jsonArray1.length(); k++) {
                                    JSONObject obj2 = jsonArray1.getJSONObject(k);
                                    ListModel listModel = new ListModel();
                                    Log.d(TAG, "obj2: " + obj2);
                                    String id = obj2.getString("id");

                                    String name = obj2.getString("name");
                                    String image = obj2.getString("image_url");
                                    String price = obj2.getString("price");
                                    String description = obj2.getString("description");
//                                    Float stars = (float) obj2.getLong("stars");
                                    String stars = obj2.getString("stars");
                                    String brands = obj2.getString("brands");

                                    listModel.setName(name);
                                    listModel.setImage(image);
                                    listModel.setPrice(price);
                                    listModel.setDescription(description);
                                    listModel.setStars(stars);
                                    listModel.setBrands(brands);
                                    listModel.setProductId(id);
                                    horizontalList.add(listModel);


                                    Log.d(TAG, "horizontalListlistModel: " + listModel);
                                    Log.d(TAG, "productlistdetails: " + name + "" + image + "" + price + "" + description + "" + stars + "" + brands);
                                }
                                list.setArrayList(horizontalList);
                                Log.d(TAG, "horizontalList: " + horizontalList);
                                listArrayList.add(list);

                                adapter = new VerticalListAdapter(getApplicationContext(), listArrayList);
                                Log.d(TAG, "listitemsadapter: " + adapter);
                                LinearLayoutManager layout_manager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layout_manager);
                                recyclerView.setAdapter(adapter);


                            }

                        }


                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }

        });

    }


//        listItemsAdapter.notifyDataSetChanged();


//        return horizontal_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenuListItems: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
//        MenuItem searchItem = menu.findItem(R.id.actionSearch);

         searchView = (SearchView) menu.findItem(R.id.activity_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        assert searchManager != null;
       /* searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);*/
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                Log.d(TAG, "onQueryTextSubmit: ");

//                adapter.getFilter().filter(query);
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(ListItems.this,
                        SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
                Log.d(TAG, "onCreatesuggestions: "+suggestions);
                suggestions.saveRecentQuery(query, null);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, "onQueryTextChange: ");
                // filter recycler view when text is changed

//                adapter.getFilter().filter(query);
                Log.d(TAG, "searchfilteradapter.getFilter().filter(query): "+query);
                return false;
            }
        });
        return true;
    }


}
