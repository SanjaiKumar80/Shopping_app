package com.example.shoppingapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.shoppingapp.adapter.ParentListAdapter;
import com.example.shoppingapp.adapter.SearchFilterAdapter;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.database.DBListHelper;
import com.example.shoppingapp.login.LoginActivity;
import com.example.shoppingapp.login.ProfileActivity;
import com.example.shoppingapp.model.ChildList;
import com.example.shoppingapp.model.ParentList;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import listener.PaginationListener;
import navigationDrawer.NavigationActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.shoppingapp.apiUrl.ApiClient.API;
import static com.example.shoppingapp.apiUrl.ApiClient.API_ITEMS;
import static com.example.shoppingapp.config.Config.noConnection;
import static listener.PaginationListener.PAGE_START;
//import static listener.PaginationListener.PAGE_START;

public class MainActivity extends NavigationActivity implements BaseSliderView.OnSliderClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.slider)
    SliderLayout mDemoSlider;

    @BindView(R.id.iv_banner_image)
    ImageView ivBannerView;

    ParentListAdapter adapter;
    ArrayList<ParentList> verticalArrayList;

 private DBListHelper dbListHelper;
    private Cursor cursor;

    SearchFilterAdapter searchFilterAdapter;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 25;
    private boolean isLoading = false;
    int itemCount = 0;

    @BindView(R.id.rv_horizontal_list)
    RecyclerView recyclerView;

    LinearLayoutManager layout_manager;

    ArrayList<ChildList> horizontal_list;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    SessionManagement sessionManagement;
    SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);

        bannerImage();
        imageSlider();
//        loadSuggestList();
        ListItems();
        setUpPagination();
        handleIntent(getIntent());
        verticalArrayList = new ArrayList<>();
        dbListHelper = new DBListHelper(this);
      /*  dbListHelper.createDatabase();

        dbListHelper.openDatabase();
        cursor=dbListHelper.getCursor();

        startManagingCursor(cursor);
*/
        recyclerView = findViewById(R.id.rv_horizontal_list);
        recyclerView.setHasFixedSize(true);
//        handleIntent(getIntent());

        sessionManagement = new SessionManagement(getApplicationContext());
        checkConnection();
        if (!sessionManagement.isLoggedIn()) {
            Log.d(TAG, "loggedin: " + sessionManagement.isLoggedIn());
            Intent intent3 = new Intent(this, LoginActivity.class);
            startActivity(intent3);
            this.finish();
        }



    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }
    @OnClick(R.id.button_show_profile)
    void showProfile(){
        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(i);

    }
    public boolean checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
        return isConnected;

    }
    //load images in sliders
    public void imageSlider(){
        Log.d(TAG, "imageSlider: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.sliderImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    final String java = response.body().string();
                    Log.d(TAG, "onResponse: "+java);
                    JSONArray array = new JSONArray(java);
                    Log.d(TAG, "onResponsearray: "+array);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject c = (JSONObject) array.get(i);

                        String title = c.getString("title");
                        Log.d(TAG, "onResponsetitle: "+title);
                        String image_url = c.getString("image_url");
                        Log.d(TAG, "onResponseimageurl: "+image_url);

                        HashMap<String, String> image_slider = new HashMap<>();
                        image_slider.put(title, image_url);

                        for (String name : image_slider.keySet()) {
                            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                            Log.d(TAG, "textsliderview: "+textSliderView);
                            // initialize a SliderLayout
                            textSliderView
//                                    .description(name)
                                    .image(image_slider.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", name);

                            mDemoSlider.addSlider(textSliderView);
                            textSliderView.setOnSliderClickListener(MainActivity.this);
                        }

                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setDuration(4000);

                    }
                }catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());

            }
        });




    }

    private void bannerImage(){
        Log.d(TAG, "bannerImage: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ITEMS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.bannerImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponsecall: ");
                try {
                    assert response.body() != null;
                    final String java = response.body().string();
                    Log.d(TAG, "onResponse: "+java);
                    JSONObject object = new JSONObject(java);
                    String images = object.getString("banner_image");
                    Log.d(TAG, "onResponseimages: "+images);
                    Picasso.with(getApplicationContext()).load(images).into(ivBannerView);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });

    }

    private void startText(String text){
//        adapter = new ParentListAdapter(this,helper.getList());
        recyclerView.setAdapter(adapter);

    }
    private void ListItems() {
        Log.d(TAG, "ListItems: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ITEMS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.horizontalListItems();
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
                    verticalArrayList = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        ParentList parentList = new ParentList();
                        JSONObject c = (JSONObject) array.get(i);

                        String name = c.getString("name");
                        Log.d(TAG, "onResponsename: " + name);
                        String scrollType = c.getString("scrolltype");
//                        String productid = c.getString("product_id");
                        parentList.setScrollType(scrollType);
                        parentList.setTitle(name);
                        JSONArray jsonArray = c.getJSONArray("id");
                        for (int k = 0;k<jsonArray.length();k++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(k);
                            String id = jsonObject.getString("product_id");
                            Log.d(TAG, "productid: "+id);

                            JSONArray object1 = jsonObject.getJSONArray("product");
                            horizontal_list = new ArrayList<>();
                            for (int j = 0; j < object1.length(); j++) {
                                ChildList horizontalItems = new ChildList();
                                JSONObject obj2 = object1.getJSONObject(j);
                                Log.d(TAG, "obj2: " + obj2);
                                String title = obj2.getString("title");
                                String image_url = obj2.getString("image_url");
//                            String product_id = obj2.getString("productid");
                                horizontal_list.add(horizontalItems);
                                horizontalItems.setListItemsTitle(title);
                                horizontalItems.setImgUrl(image_url);
                                horizontalItems.setProductid(id);

                            }
                        }


                        parentList.setArrayList(horizontal_list);
                        verticalArrayList.add(parentList);
                        adapter = new ParentListAdapter(getApplicationContext(), verticalArrayList);
                        Log.d(TAG, "ParentListAdaptervertical: "+adapter);
                        layout_manager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layout_manager);
                        recyclerView.setAdapter(adapter);

                        /*  * manage progress view
                         * */
                        if (currentPage != PAGE_START) adapter.removeLoading();
                        Log.d(TAG, "adapter.addItems(verticalArrayList): "+adapter);
//                        adapter.addItems(verticalArrayList);

                        // check weather is last page or not
                        if (currentPage < totalPage) {
                            adapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

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


    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: ");
        intent = new Intent();
        intent.setAction("android.intent.action.SEARCH");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d(TAG, "searchManager: "+SearchManager.QUERY);
//            intent.putExtra("query",SearchManager.QUERY);

//            doMySearch(query);
        }
    }


//    private void doMySearch(String searchQuery) {
//        textView.setText(searchQuery);
//    }

    public void setUpPagination(){
        layout_manager = new LinearLayoutManager(this);
        layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout_manager);
        recyclerView.setNestedScrollingEnabled(false);
        Log.d(TAG, "setUpPagination: "+layout_manager);
        recyclerView.addOnScrollListener(new PaginationListener( layout_manager) {
            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: ");
                isLoading = true;
                currentPage++;
                ListItems();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_search, menu);

         searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                Log.d(TAG, "onQueryTextSubmit: ");

                adapter.getFilter().filter(query);
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(MainActivity.this,
                        SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
                Log.d(TAG, "onCreatesuggestions: "+suggestions);
                dbListHelper.insertToUser(suggestions);
                Log.d(TAG, "dbListHelper.insertToUser(suggestions): ");
                suggestions.saveRecentQuery(query, null);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, "onQueryTextChange: ");
                // filter recycler view when text is changed

                adapter.getFilter().filter(query);
                Log.d(TAG, "searchfilteradapter.getFilter().filter(query): "+query);

                return false;
            }
        });
        //  for suggestion list
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                String suggestion = getSuggestion(position);
                searchView.setQuery(suggestion, true); // submit query now
                return true; // replace default search manager behaviour

            }
        });

return true;
    }
    private String getSuggestion(int position) {
        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(
                position);
        String suggest1 = cursor.getString(cursor
                .getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        return suggest1;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }




    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            noConnection(MainActivity.this);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void onRefresh() {

    }
}
