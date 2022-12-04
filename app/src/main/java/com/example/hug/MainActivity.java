package com.example.hug;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hug.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_donate, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_donate:
                    {
                        if (GlobalVariables.user_id != null) {
                            navController.navigate(R.id.navigation_donate);
                        } else {
                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                            alertDialogBuilder.setTitle("BECOME A DONOR !");
                            alertDialogBuilder.setMessage("You have to Sign In or Create an account to become a Donor!");
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    GlobalVariables.donateNav = 1;
                                    navController.navigate(R.id.navigation_login);

                                }
                            });
                            alertDialogBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialogBuilder.show();
                        }
                        break;
                    }
                    case R.id.navigation_login:
                    {
                        navController.navigate(R.id.navigation_login);
                        break;
                    }
                    case R.id.navigation_profile:
                    {
                        if (GlobalVariables.user_id != null) {
                            navController.navigate(R.id.navigation_profile);
                        }else{
                            GlobalVariables.profileNav = 1;
                            navController.navigate(R.id.navigation_login);
                        }
                        break;
                    }
                    case R.id.navigation_search:
                    {
                        navController.navigate(R.id.navigation_search);
                        break;
                    }

                }
                return true;
            }
        });
    }


}