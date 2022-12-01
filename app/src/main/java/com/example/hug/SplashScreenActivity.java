package com.example.hug;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.example.hug.databinding.ActivitySplashScreenBinding;
import com.example.hug.models.LocationModel;
import com.example.hug.ui.APIClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                getLocationData();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void getLocationData(){
        Call<List<LocationModel>> request =  APIClient.getLocationService().getAllLocations();
        request.enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, retrofit2.Response<List<LocationModel>> response) {
                if (response.isSuccessful()) {
                    List<LocationModel> locations = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("Search_Locations", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(locations);
                    editor.putString("locations", json);
                    editor.apply();

                    redirect();
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to load data. " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                redirect();
            }
        });

    }

    private void redirect(){
        startActivity(new Intent(this, MainActivity.class));
    }
}