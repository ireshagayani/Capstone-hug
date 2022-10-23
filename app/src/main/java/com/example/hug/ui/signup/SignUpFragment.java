package com.example.hug.ui.signup;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hug.R;
import com.example.hug.ui.APIClient;
import com.example.hug.ui.login.LoginResponse;
import com.example.hug.ui.login.LoginViewModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private MaterialButton loginBtn;
    private MaterialButton signUpBtn;
    private EditText signUpUsername;
    private EditText signUpPassword;
    private EditText signUpConPassword;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        signUpUsername = view.findViewById(R.id.signup_username_textInputEditText);
        signUpPassword = view.findViewById(R.id.signup_password_textInputEditText);
        signUpConPassword = view.findViewById(R.id.signup_confirm_password_textInputEditText);
        loginBtn = view.findViewById(R.id.signup_login_btn);
        signUpBtn = view.findViewById(R.id.signup_signup_btn);

        loginBtn.setOnClickListener(loginBtnClickListener());
        signUpBtn.setOnClickListener(signUpBtnClickListner());

        return view;
    }

    private View.OnClickListener signUpBtnClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = signUpUsername.getText().toString();
                String password = signUpPassword.getText().toString();
                String conPassword = signUpConPassword.getText().toString();

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(conPassword) || !TextUtils.equals(password,conPassword)){
                    Toast.makeText(getContext(), "Please provide required details!",
                            Toast.LENGTH_SHORT).show();

                }
                else{
                    signUp();
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }


    private View.OnClickListener loginBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_signup_to_navigation_login);
            }
        };
    }

    public void signUp() {

        SignUpViewModel signUpViewModel = new SignUpViewModel();

        String userName = signUpUsername.getText().toString();
        String password = signUpPassword.getText().toString();

        signUpViewModel.setUsername(userName);
        signUpViewModel.setPassword(password);
        signUpViewModel.setFirstName("");
        signUpViewModel.setLastName("");
        signUpViewModel.setLocationId("0");

        Call<SignUpResponse> signUpResponseCall = APIClient.getUserService().userSignUp(signUpViewModel);
        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "SignUp Successful.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "SignUp failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}