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
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private MaterialButton loginBtn;
    private MaterialButton signUpBtn;
    private EditText signUpUsername,signUpPassword,signUpConPassword,signUpName,signUpPhone,signUpEmail;
    private String userName,password,conPassword,name,phone,email;
    private static final String email_regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Pattern email_pattern = Pattern.compile(email_regex,Pattern.CASE_INSENSITIVE);

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //signUpUsername = view.findViewById(R.id.signup_username_textInputEditText);
        signUpPassword = view.findViewById(R.id.signup_password_textInputEditText);
        signUpConPassword = view.findViewById(R.id.signup_confirm_password_textInputEditText);
        signUpName = view.findViewById(R.id.signup_firstname_textInputEditText);
        signUpPhone = view.findViewById(R.id.signup_phone_textInputEditText);
        signUpEmail = view.findViewById(R.id.signup_email_textInputEditText);
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
                //userName = signUpUsername.getText().toString();
                password = signUpPassword.getText().toString();
                conPassword = signUpConPassword.getText().toString();
                name = signUpName.getText().toString();
                phone = signUpPhone.getText().toString();
                email = signUpEmail.getText().toString();

                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(conPassword) ||
                        TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)){
                    Snackbar snackbar = Snackbar.make(view, "Required Fields cannot be empty!" , Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                else {
                    if(!TextUtils.equals(password,conPassword)){
                        Snackbar snackbar = Snackbar.make(view, "Passwords do not match!" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else if(!PASSWORD_PATTERN.matcher(password).matches()){

                        Snackbar snackbar = Snackbar.make(view, "Password should be at least 4 characters long and have 1 special character!" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else if(!email_pattern.matcher(email.trim()).matches()){
                        signUpEmail.setError("Invalid Email Address!");
                        Snackbar snackbar = Snackbar.make(view, "Invalid Email Address!"+email , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else{
                        signUp(view);
                    }
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

    public void signUp(View view) {

        SignUpViewModel signUpViewModel = new SignUpViewModel();

        signUpViewModel.setUsername(email);
        signUpViewModel.setPassword(password);
        signUpViewModel.setFirstName(name);
        signUpViewModel.setLastName("");
        signUpViewModel.setLocationId("0");
        signUpViewModel.setPhone(phone);
        //signUpViewModel.setEmail(email);

        Call<SignUpResponse> signUpResponseCall = APIClient.getUserService().userSignUp(signUpViewModel);
        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if(response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(view, "Signed up Successfully!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Navigation.findNavController(view).navigate(R.id.action_navigation_signup_to_navigation_login);
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "Signed up Failed!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //signUpUsername.setText("");
                    signUpPassword.setText("");
                    signUpConPassword.setText("");
                    signUpName.setText("");
                    signUpPhone.setText("");
                    signUpEmail.setText("");
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}