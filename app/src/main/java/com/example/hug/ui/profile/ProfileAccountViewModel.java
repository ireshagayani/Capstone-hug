package com.example.hug.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.hug.models.LocationModel;

public class ProfileAccountViewModel extends ViewModel {

    private LocationModel location;
    private String username;
    private String password;


    public LocationModel getLocation() { return location; }
    public void setLocation(LocationModel location) { location = location; }

    public String getUsername() { return username; }
    public void setUsername(String username) { username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { password = password; }
}