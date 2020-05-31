package com.example.shoppingapp.apiUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    public static final  String BASE_URL = "http://tonyrajesh.com/androidserver.tonyrajesh.com/android-sridevi/shopping/";
    public static  final String API  = "http://android.tonyrajesh.com/";
    public static  final String API_ITEMS  = "http://android.tonyrajesh.com/android_sri/";
    private static Retrofit retrofit = null;



    public static Retrofit getClient() {
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(150, TimeUnit.SECONDS);
            client.readTimeout(150, TimeUnit.SECONDS);
            client.writeTimeout(150, TimeUnit.SECONDS);
            client.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }




}


