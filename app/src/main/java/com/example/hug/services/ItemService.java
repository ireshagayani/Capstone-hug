package com.example.hug.services;


import com.example.hug.models.ItemModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService {
    @GET("api/Item/")
    Call<List<ItemModel>> getAllItemsByLocationId(@Query("locationId") Integer locationId);

    @PUT("api/Item/{id}")
    Call<ItemModel> updateItem(@Path("id") Integer id, @Body ItemModel donateViewModel);
}
