package com.example.hug.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hug.R;
import com.example.hug.helper.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProfileAccountFragment extends Fragment {

    private ProfileAccountViewModel mViewModel;
    private TextView credentialEditBtn, locationEditBtn;
    private BottomSheetDialog bottomSheetDialog;

    public static ProfileAccountFragment newInstance() {
        return new ProfileAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_account, container, false);


        credentialEditBtn = view.findViewById(R.id.profile_account_credentials_edit_btn);

        credentialEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_profile_account, null);

                sheetview.findViewById(R.id.profile_account_close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.hide();
                    }
                });


                sheetview.findViewById(R.id.profile_account_update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(isValidAccountForm(sheetview)){
                            Toast.makeText(getContext(), "Form valid", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        });


        locationEditBtn = view.findViewById(R.id.profile_account_location_edit_btn);

        locationEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_profile_location, null);

                ArrayAdapter<String> locationTypeDropdownAdapter = new ArrayAdapter<String>(getContext(), R.layout.autocomplete_item, getLocationTypes());
                AutoCompleteTextView locationTypeDropdown = sheetview.findViewById(R.id.profile_location_type_dropDown);
                locationTypeDropdown.setAdapter(locationTypeDropdownAdapter);

                ArrayAdapter<String> provincesDropdownAdapter = new ArrayAdapter<String>(getContext(), R.layout.autocomplete_item, Constants.provinces);
                AutoCompleteTextView provincesDropdown = sheetview.findViewById(R.id.profile_location_province_dropDown);
                provincesDropdown.setAdapter(provincesDropdownAdapter);



                sheetview.findViewById(R.id.profile_location_close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.hide();
                    }
                });


                sheetview.findViewById(R.id.profile_location_update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValidLocationForm(sheetview)){

                        }
                    }
                });

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileAccountViewModel.class);
        // TODO: Use the ViewModel
    }


    private View.OnClickListener editBtnOnClickHandler(String btnKey){
      return new View.OnClickListener() {
          @Override
          public void onClick(View view) {

          }
      }  ;
    };


    private ArrayList<String> getLocationTypes(){
        ArrayList<String> result = new ArrayList<String>();
        result.add("Home");
        result.add("Organization");

        return result;
    }

    private boolean isValidAccountForm(View view){
        TextInputEditText password = view.findViewById(R.id.profile_account_password_textInputEditText);
        TextInputEditText confirmPassword = view.findViewById(R.id.profile_account_confirm_password_textInputEditText);

        if(password.getText() != null && password.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(confirmPassword.getText() != null && confirmPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Confirm Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.getText().toString() != confirmPassword.getText().toString() ){
            Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidLocationForm(View view){
        TextInputEditText name = view.findViewById(R.id.profile_location_name_textInputEditText);
        AutoCompleteTextView locationType = view.findViewById(R.id.profile_location_type_dropDown);
        TextInputEditText address = view.findViewById(R.id.profile_location_address_textInputEditText);
        TextInputEditText city = view.findViewById(R.id.profile_location_city_textInputEditText);
        AutoCompleteTextView province = view.findViewById(R.id.profile_location_province_dropDown);
        TextInputEditText postalCode = view.findViewById(R.id.profile_location_postalcode_textInputEditText);

        if(name.getText() != null && name.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(locationType.getText() != null && locationType.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Location type is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(address.getText() != null && address.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Address is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(city.getText() != null && city.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "City is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(province.getText() != null && province.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Province is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(postalCode.getText() == null || postalCode.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Postal Code is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(postalCode.getText() != null){
            String postalCodeRegEx = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";

            if(Pattern.compile(postalCodeRegEx).matcher(postalCode.getText().toString()).matches()){
                Toast.makeText(getContext(), "Postal Code is invalid.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


}