package com.example.hug.ui;

import com.example.hug.services.ItemService;
import com.example.hug.services.LocationService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit getRetrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://hug-conestoga.azurewebsites.net/")
//                .baseUrl("http://10.0.2.2:5166/")
                .client(okHttpClient)
                .build();
    }

    public static APICall getUserService(){

        return getRetrofit().create(APICall.class);
    }

    public static LocationService getLocationService(){
        return getRetrofit().create(LocationService.class);
    }

    public static ItemService getItemService(){
        return getRetrofit().create(ItemService.class);
    }

}
