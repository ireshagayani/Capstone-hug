package com.example.hug.ui.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    private Integer user_id;

    public LoginResponse(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
