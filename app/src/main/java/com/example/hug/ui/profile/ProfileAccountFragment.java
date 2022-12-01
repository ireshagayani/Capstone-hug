package com.example.hug.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hug.GlobalVariables;
import com.example.hug.R;
import com.example.hug.helper.Constants;
import com.example.hug.models.LocationModel;
import com.example.hug.ui.APIClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAccountFragment extends Fragment {

    private View view;
    private ProfileAccountViewModel mViewModel;
    private TextView credentialEditBtn, locationEditBtn;
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout locationAddHeader, locationEditHeader;
    private ArrayAdapter<String> locationTypeDropdownAdapter, provincesDropdownAdapter;
    private MaterialButton addLocationBtn;
    private TextView locationNameTextView, locationTypeTextView, addressTextView, cityTextView, provinceTextView, postalCodeTextView;
    private LocationModel currentLocation;
    private ProfileAccountViewModel profileAccount;
    private TextInputEditText locationNameTextInputEditText, addressTextInputEditText, cityTextInputEditText, postalCodeTextInputEditText;
    private AutoCompleteTextView locationTypeAutoCompleteTextView, provinceAutoCompleteTextView;
    private Integer user_id = GlobalVariables.user_id;

    //Bottomsheet location
    private TextInputEditText sheetLocationName, sheetLocationAddress, sheetLocationCity, sheetLocationPostalCode, sheetLocationPhone;
    private AutoCompleteTextView sheetLocationLocationType, sheetLocationProvince;

    public static ProfileAccountFragment newInstance() {
        return new ProfileAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_account, container, false);
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetTheme);

        locationTypeDropdownAdapter = new ArrayAdapter<String>(getContext(), R.layout.autocomplete_item, Constants.profileLocationTypes);
        provincesDropdownAdapter = new ArrayAdapter<String>(getContext(), R.layout.autocomplete_item, Constants.provinces);

        locationEditBtn = view.findViewById(R.id.profile_account_location_edit_btn);
        locationEditBtn.setOnClickListener(addEditLocationOnClickHandler());

        credentialEditBtn = view.findViewById(R.id.profile_account_credentials_edit_btn);
        credentialEditBtn.setOnClickListener(EditCredentialsOnClickHandler());

        addLocationBtn = view.findViewById(R.id.profile_location_add_btn);

        //Toast.makeText(getContext(), "userId1:"+GlobalVariables.user_id, Toast.LENGTH_SHORT).show();
        reloadAccountView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadLocationData(5);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileAccountViewModel.class);
        // TODO: Use the ViewModel
    }

    private boolean isValidAccountForm(View view) {
        TextInputEditText password = view.findViewById(R.id.profile_account_password_textInputEditText);
        TextInputEditText confirmPassword = view.findViewById(R.id.profile_account_confirm_password_textInputEditText);

        if (password.getText() != null && password.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirmPassword.getText() != null && confirmPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Confirm Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString() != confirmPassword.getText().toString()) {
            Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidLocationForm(View view) {
        TextInputEditText name = view.findViewById(R.id.profile_location_name_textInputEditText);
        AutoCompleteTextView locationType = view.findViewById(R.id.profile_location_type_dropDown);
        TextInputEditText address = view.findViewById(R.id.profile_location_address_textInputEditText);
        TextInputEditText city = view.findViewById(R.id.profile_location_city_textInputEditText);
        AutoCompleteTextView province = view.findViewById(R.id.profile_location_province_dropDown);
        TextInputEditText postalCode = view.findViewById(R.id.profile_location_postalcode_textInputEditText);

        if (name.getText() != null && name.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Name is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (locationType.getText() != null && locationType.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Location type is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.getText() != null && address.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Address is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (city.getText() != null && city.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "City is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (province.getText() != null && province.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Province is required.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (postalCode.getText() == null && postalCode.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Postal Code is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if (postalCode.getText() != null) {
//            String postalCodeRegEx = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
//
//            if (Pattern.compile(postalCodeRegEx).matcher(postalCode.getText().toString()).matches()) {
//                Toast.makeText(getContext(), "Postal Code is invalid.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }

        return true;
    }

    private View.OnClickListener EditCredentialsOnClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        if (isValidAccountForm(sheetview)) {
                            Toast.makeText(getContext(), "Form valid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        };
    }

    private View.OnClickListener addEditLocationOnClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_profile_location, null);
                AutoCompleteTextView locationTypeDropdown = sheetview.findViewById(R.id.profile_location_type_dropDown);
                AutoCompleteTextView provincesDropdown = sheetview.findViewById(R.id.profile_location_province_dropDown);

                locationTypeDropdown.setAdapter(locationTypeDropdownAdapter);
                provincesDropdown.setAdapter(provincesDropdownAdapter);

                initializeAddEditLoction(sheetview, currentLocation);

                sheetview.findViewById(R.id.profile_location_close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.hide();
                    }
                });


                sheetview.findViewById(R.id.profile_location_update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValidLocationForm(sheetview)) {
                            ProfileAccountLocationViewModel location = new ProfileAccountLocationViewModel();
//                            location.setId((currentLocation != null && currentLocation.getId() > 0 ? currentLocation.getId() : null));
//                            location.setLocationType(locationTypeAutoCompleteTextView.getText().toString());
//                            location.setAddress(addressTextInputEditText.getText().toString());
//                            location.setCity(cityTextInputEditText.getText().toString());
//                            location.setProvince(provinceAutoCompleteTextView.getText().toString());
//                            location.setPostalCode(postalCodeTextInputEditText.getText().toString());
//                            location.setModifiedBy(5);
//                            location.setUserId(5);

                            addUpdateLocation(location);
                        }
                    }
                });

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        };
    }

    private void reloadAccountView() {
        //currentLocation = loadLocationData(user_id);

        profileAccount = new ProfileAccountViewModel();
        ProfileAccountLocationViewModel profileAccountLocationViewModel = new ProfileAccountLocationViewModel();
        //Toast.makeText(getContext(), "signed in as :"+user_id, Toast.LENGTH_SHORT).show();
        if(user_id != null){
            currentLocation = loadLocationData(user_id);
            profileAccount.setLocation(currentLocation);
            initializeAccountView(profileAccount);
        }
        else{
            Toast.makeText(getContext(), "You have to Sign In or Create an account to view your profile!", Toast.LENGTH_SHORT).show();
        }


    }


    private void initializeAccountView(ProfileAccountViewModel accountViewModel) {
        locationAddHeader = view.findViewById(R.id.profile_account_location_add_header);
        locationEditHeader = view.findViewById(R.id.profile_account_location_edit_header);

        //Credentials
        TextView credentialsUsernameTextView = view.findViewById(R.id.profile_account_username_textView);
        TextView credentialsPasswordTextView = view.findViewById(R.id.profile_account_password_textView);

        credentialsUsernameTextView.setText(accountViewModel.getUsername());
        credentialsPasswordTextView.setText(accountViewModel.getPassword());
    }

    private void initilizeLocationView(LocationModel location) {
        currentLocation = location;
        //Location

        Toast.makeText(getContext(), "location: "+currentLocation, Toast.LENGTH_SHORT).show();

        if (location != null && location.getId() != null) {

            locationNameTextView = view.findViewById(R.id.profile_account_location_name_textView);
            locationTypeTextView = view.findViewById(R.id.profile_account_location_type_textView);
            addressTextView = view.findViewById(R.id.profile_account_location_address_textView);
            cityTextView = view.findViewById(R.id.profile_account_location_city_textView);
            provinceTextView = view.findViewById(R.id.profile_account_location_province_textView);
            postalCodeTextView = view.findViewById(R.id.profile_account_location_postalcode_textView);

            //Toast.makeText(getContext(), "province: "+location.getProvince(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getContext(), "postal: "+location.getPostalCode(), Toast.LENGTH_SHORT).show();

            locationNameTextView.setText(location.getName());
            locationTypeTextView.setText(location.getLocationType());
            addressTextView.setText(location.getAddress());
            cityTextView.setText(location.getCity());
            provinceTextView.setText(location.getProvince());
            postalCodeTextView.setText(location.getPostalCode());

            locationAddHeader.setVisibility(View.GONE);
            locationEditHeader.setVisibility(View.VISIBLE);
        } else {

            addLocationBtn.setOnClickListener(addEditLocationOnClickHandler());

            locationEditHeader.setVisibility(View.GONE);
            locationAddHeader.setVisibility(View.VISIBLE);
        }
    }

    private void initializeAddEditLoction(View view, LocationModel location) {

        locationNameTextInputEditText = view.findViewById(R.id.profile_location_name_textInputEditText);
        locationTypeAutoCompleteTextView = view.findViewById(R.id.profile_location_type_dropDown);
        addressTextInputEditText = view.findViewById(R.id.profile_location_address_textInputEditText);
        cityTextInputEditText = view.findViewById(R.id.profile_location_city_textInputEditText);
        provinceAutoCompleteTextView = view.findViewById(R.id.profile_location_province_dropDown);
        postalCodeTextInputEditText = view.findViewById(R.id.profile_location_postalcode_textInputEditText);

        if (location != null && location.getId() != null) {
            locationNameTextInputEditText.setText(location.getName());
            locationTypeAutoCompleteTextView.setText(location.getLocationType());
            addressTextInputEditText.setText(location.getAddress());
            cityTextInputEditText.setText(location.getCity());
            provinceAutoCompleteTextView.setText(location.getProvince());
            postalCodeTextInputEditText.setText(location.getPostalCode());

            locationAddHeader.setVisibility(View.GONE);
            locationEditHeader.setVisibility(View.VISIBLE);
        }
    }

    private void addUpdateLocation(ProfileAccountLocationViewModel locationModel) {
//        Call<LocationModel> request = (locationModel.getId() != null && locationModel.getId() > 0) ?
//                                        APIClient.getLocationService().updateLocation(locationModel.getId(), locationModel) :
//                                        APIClient.getLocationService().insertLocation(locationModel);

        Call<LocationModel> request =  APIClient.getLocationService().insertLocation(locationModel);

        request.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private LocationModel loadLocationData(int id) {
        LocationModel location = new LocationModel();
        Call<LocationModel> request = APIClient.getLocationService().getLocationByUserId(id);

        request.enqueue(new Callback<LocationModel>() {

            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.isSuccessful()) {
                    LocationModel aa = response.body();

                    initilizeLocationView(aa);
                }
                else {
                    Snackbar snackbar = Snackbar.make(view, "Location details Failed!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return location;
    }
}