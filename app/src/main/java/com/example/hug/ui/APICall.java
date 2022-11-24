package com.example.hug.ui;

import com.example.hug.ui.donate.DonateResponse;
import com.example.hug.ui.donate.DonateViewModel;
import com.example.hug.ui.login.LoginResponse;
import com.example.hug.ui.login.LoginViewModel;
import com.example.hug.ui.signup.SignUpResponse;
import com.example.hug.ui.signup.SignUpViewModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APICall {

    @POST("api/auth/")
    Call<LoginResponse> userLogin(@Body LoginViewModel loginViewModel);

    @POST("api/User/")
    Call<SignUpResponse> userSignUp(@Body SignUpViewModel signUpViewModel);

    @POST("api/item/")
    Call<DonateResponse> donateItem(@Body DonateViewModel donateViewModel);
}
