package com.example.shoppingapp.products;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.ProductListAdapter;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.model.ParentList;
import com.example.shoppingapp.model.Products;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.shoppingapp.apiUrl.ApiClient.API_ITEMS;

public class ProductDetails extends AppCompatActivity {
    private static final String TAG = ProductDetails.class.getSimpleName();
    ProductListAdapter adapter;
    List<Products> productsList;
    LinearLayoutManager layout_manager;
    @BindView(R.id.recycler_view_products_details)
    RecyclerView recyclerViewProduct;
    String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        recyclerViewProduct.setHasFixedSize(true);
         productId = getIntent().getStringExtra("productitemsId");

        productDetails();

    }
    private void productDetails(){
        Log.d(TAG, "productDetailsId: "+productId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ITEMS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.productDetails( productId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                assert response.body() != null;

                try {
                    final String java = response.body().string();
                    Log.d(TAG, "onResponsesend: " + java);
                    JSONObject object = new JSONObject(java);
                    JSONArray array = object.getJSONArray("product_details");
                    Log.d(TAG, "onResponsearray: " + array);
                    productsList= new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        Products products = new Products();
                        JSONObject c = (JSONObject) array.get(i);
                        String id = c.getString("product_id");
                        Log.d(TAG, "onResponseid: "+id);
                        Log.d(TAG, "onResponseproductId: "+productId);
                        if (id.equals(productId)) {
                            String productname = c.getString("name");
                            String productdescription = c.getString("description");
                            String productimgurl = c.getString("image_url");
                            String productprice = c.getString("mrp_price");
                            String productdiscountprice = c.getString("discount_price");
                            String productsave = c.getString("save");
                            String productstars = c.getString("stars");

                            products.setName(productname);
                            products.setDescription(productdescription);
                            products.setImage_url(productimgurl);
                            products.setDiscountPrice(productdiscountprice);
                            products.setMrpPrice(productprice);
                            products.setSave(productsave);
                            products.setStars(productstars);
                            productsList.add(products);
                            adapter = new ProductListAdapter(productsList, getApplicationContext());
                            layout_manager = new LinearLayoutManager(getApplicationContext());
                            recyclerViewProduct.setLayoutManager(layout_manager);
                            recyclerViewProduct.setAdapter(adapter);


                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
