package com.example.hug.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hug.GlobalVariables;
import com.example.hug.R;
import com.example.hug.ui.APIClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText loginUsername;
    private EditText loginPassword;
    String userName = "";
    String password = "";

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        loginUsername = view.findViewById(R.id.login_username_textInputEditText);
        loginPassword = view.findViewById(R.id.login_password_textInputEditText);
        MaterialButton signUpBtn = view.findViewById(R.id.login_sign_up_btn);
        MaterialButton loginBtn = view.findViewById(R.id.login_login_btn);

        signUpBtn.setOnClickListener(signUpBtnClickListener());
        loginBtn.setOnClickListener(loginBtnClickListner());

        return view;
    }

    private View.OnClickListener loginBtnClickListner() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = loginUsername.getText().toString();
                password = loginPassword.getText().toString();

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(), "Username/Password is required!",
                            Toast.LENGTH_SHORT).show();

                }
                else{
                    login(view);
                }
            }
        };

    }

    public void login(View view) {

        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.setUsername(userName);
        loginViewModel.setPassword(password);

        Call<LoginResponse> loginResponseCall = APIClient.getUserService().userLogin(loginViewModel);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    GlobalVariables.user_id = response.body().getUser_id();
                    Snackbar snackbar = Snackbar.make(view, "Logged In Successfully!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    if(GlobalVariables.donateNav == true){
                        Navigation.findNavController(view).navigate(R.id.navigation_donate);
                    }
                    else if(GlobalVariables.profileNav == true){
                        Navigation.findNavController(view).navigate(R.id.navigation_profile);
                    }
                    else{
                        Navigation.findNavController(view).navigate(R.id.navigation_search);
                    }
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "Logged In Failed!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call,Throwable t) {
                Toast.makeText(getContext(), "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private View.OnClickListener signUpBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_navigation_login_to_navigation_signup);
            }
        };
    }

}