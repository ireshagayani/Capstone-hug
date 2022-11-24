package com.example.hug.ui.donate;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hug.R;
import com.example.hug.ui.APIClient;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonateFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    private String apiKey = "AIzaSyC9MTMF1rU79kxeii_wV_urfiMnfTR0Dx8";
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields;
    private DonateViewModel mViewModel;

    public static DonateFragment newInstance() {
        return new DonateFragment();
    }

    TextInputEditText date_input;
    TextInputEditText time_input;
    TextInputEditText location_input;
    TextInputEditText title_input;
    TextInputEditText quantity_input;
    TextInputEditText instructions_input;
    RecyclerView recyclerView;
    ImageButton imageButton;
    MaterialButton donateAddButton;
    String title = "";
    Integer qty = 0;
    String location = "";
    String date = "";
    String instructions = "";
    String addressLine2 = "";
    String city = "";
    String province = "";
    String country = "";
    String postalCode = "";
    StringBuilder addressLine1 = new StringBuilder();
    Integer createdBy;
    Integer userId;

    ArrayList<Uri> arrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donate, container, false);

        //datepicker

        date_input = v.findViewById(R.id.date_textInputEditText);
        date_input.setInputType(InputType.TYPE_NULL);

        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(date_input);
            }
        });

        //location

        if(!Places.isInitialized()){
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }

        //create a new Places client instance
        PlacesClient placesClient = Places.createClient(getContext());

        location_input = v.findViewById(R.id.location_textInputEditText);

        location_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME,Place.Field.ADDRESS_COMPONENTS);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY , fields)
                        .build(getContext());
                startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        //image picker

        recyclerView = v.findViewById(R.id.recycler_view);
        imageButton = v.findViewById(R.id.add_imageButton1);

        imageButton.setOnClickListener(view -> {
            String[] strings = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

            if(EasyPermissions.hasPermissions(v.getContext(),strings)){
                imagePicker();
            }
            else{
                EasyPermissions.requestPermissions(
                        this,
                        "App needs access to your camera and storage",
                        100,
                        strings
                );
            }
        });

        donateAddButton = v.findViewById(R.id.donate_create_btn);
        title_input = v.findViewById(R.id.title_textInputEditText);
        quantity_input = v.findViewById(R.id.quantity_textInputEditText);
        location_input = v.findViewById(R.id.location_textInputEditText);
        date_input = v.findViewById(R.id.date_textInputEditText);
        instructions_input = v.findViewById(R.id.instructions_textInputEditText);
        
        donateAddButton.setOnClickListener(donateAddButtonClickListner());

        return v;
    }

    private void showDateTimeDialog(TextInputEditText date_input) {

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY,i);
                        calendar.set(Calendar.MINUTE,i1);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E,MMM dd yyyy HH:mm");
                        date_input.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getContext(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(getContext(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private View.OnClickListener donateAddButtonClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_input.getText().toString();
                qty = Integer.valueOf(quantity_input.getText().toString());
                location = location_input.getText().toString();
                date = date_input.getText().toString() ;
                instructions = instructions_input.getText().toString();

                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(quantity_input.getText().toString()) || TextUtils.isEmpty(location) || TextUtils.isEmpty(date)){

                    Toast.makeText(getContext(), "Enter all the required fields!", Toast.LENGTH_SHORT).show();
                }

                else{

                    donateCreate(view);
                }
            }
        };
    }

    private void donateCreate(View view) {

        DonateViewModel donateViewModel = new DonateViewModel();
        LocationViewModel locationViewModel;
        donateViewModel.setName(title);
        donateViewModel.setQty(qty);
        donateViewModel.setPickupDateTime(date);
        donateViewModel.setPickupInstruction(instructions);
        donateViewModel.setItemStatus("Available");
        locationViewModel = new LocationViewModel(7,7,addressLine1,addressLine2,city,province,country,postalCode);
        donateViewModel.setLocation(locationViewModel);
        donateViewModel.setCreatedBy(7);
        donateViewModel.setUserId(7);

        Call<DonateResponse> donateResponseCall = APIClient.getUserService().donateItem(donateViewModel);
        donateResponseCall.enqueue(new Callback<DonateResponse>() {
            @Override
            public void onResponse(Call<DonateResponse> call, Response<DonateResponse> response) {
                if(response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(view, "Donation Item added Successfully!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    title_input.setText("");
                    quantity_input.setText("");
                    location_input.setText("");
                    date_input.setText("");
                    instructions_input.setText("");
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "Item creation Failed!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<DonateResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null){
            arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
            recyclerView.setAdapter(new RecyclerAdapter(arrayList));
        }

        else if(requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Place place = Autocomplete.getPlaceFromIntent(data);
            location_input.setText(place.getAddress());
            Log.i(TAG, "Place: " +place.getAddressComponents());
            addressComponents(place);
        }

        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
        }

    }

    private void addressComponents(Place place) {
        AddressComponents components = place.getAddressComponents();

        if(components != null){
            for(AddressComponent component : components.asList()){
                String type = component.getTypes().get(0);
                switch (type){
                    case "street_number": {
                        addressLine1.insert(0,component.getName());
                        break;
                    }
                    case "route":{
                        addressLine1.append(" ");
                        addressLine1.append(component.getName());
                        break;
                    }
                    case "subpremise":{
                        addressLine2 = component.getName();
                        break;
                    }
                    case "locality": {
                        city = component.getName();
                        break;
                    }
                    case "administrative_area_level_1": {
                        province = component.getShortName();
                        break;
                    }
                    case "country": {
                        country = component.getName();
                        break;
                    }
                    case "postal_code":
                        postalCode = component.getName();
                        break;

                }
            }
        }
    }

    private void imagePicker() {
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN,3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN,4)
                .setMaxCount(12)
                .setSelectedFiles(arrayList)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DonateViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == 100 && perms.size() == 2){
            imagePicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
        else{
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}