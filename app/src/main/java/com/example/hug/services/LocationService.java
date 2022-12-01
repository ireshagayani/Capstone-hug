package com.example.hug.services;

import com.example.hug.models.LocationModel;
import com.example.hug.ui.profile.ProfileAccountLocationViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface LocationService {

    @POST("api/Location")
    Call<LocationModel> insertLocation(@Body ProfileAccountLocationViewModel locationModel);

    @PUT("api/Location/{id}")
    Call<LocationModel> updateLocation(@Path("id") Integer id, @Body ProfileAccountLocationViewModel locationModel);

    @GET("api/Location/{id}")
    Call<LocationModel> getLocationByUserId(@Path("id") Integer id);

    @GET("api/Location")
    Call<List<LocationModel>> getAllLocations();
}
