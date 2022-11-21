package com.example.hug.ui.donate;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hug.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Result;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.AppSettingsDialogHolderActivity;
import pub.devrel.easypermissions.EasyPermissions;

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

    ArrayList<Uri> arrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donate, container, false);

        //datepicker

        date_input = v.findViewById(R.id.date_textInputEditText);

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("Pickup Date")
                .build();

        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                date_input.setText(datePicker.getHeaderText());
            }
        });

        //timepicker

        time_input = v.findViewById(R.id.time_textInputEditText);

        Calendar calendar = Calendar.getInstance();
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("PickupTime")
                .build();

        time_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getActivity().getSupportFragmentManager(),"TIME_PICKER");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hour = timePicker.getHour();
                        int Minute = timePicker.getMinute();
                        time_input.setText(Hour+":"+Minute);
                    }
                });
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
        time_input = v.findViewById(R.id.time_textInputEditText);
        instructions_input = v.findViewById(R.id.instructions_textInputEditText);
        
        //donateAddButton.setOnClickListener(donateAddButtonClickListner());

        return v;
    }

//    private View.OnClickListener donateAddButtonClickListner() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String title = title_input.getText().toString();
//                String qty = quantity_input.getText().toString();
//                String location = location_input.getText().toString();
//                String date = date_input.getText().toString();
//                String time = time_input.getText().toString();
//                String instructions = instructions_input.getText().toString();
//
//                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(qty) || TextUtils.isEmpty(location) || TextUtils.isEmpty(date)
//                        || TextUtils.isEmpty(time)){
//
//                    Toast.makeText(getContext(), "Enter all the required fields!", Toast.LENGTH_SHORT).show();
//                }
//
//                else{
//                    donateCreate(view);
//                }
//            }
//        };
//    }

//    private void donateCreate(View view) {
//    }

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
        StringBuilder address = new StringBuilder();

        if(components != null){
            for(AddressComponent component : components.asList()){
                String type = component.getTypes().get(0);
                switch (type){
                    case "street_number": {
                        address.insert(0,component.getName());
                        break;
                    }
                    case "subpremise":
                    case "route":{
                        address.append(" ");
                        address.append(component.getName());
                        break;
                    }
                    case "locality": {
                        String city = component.getName();
                        break;
                    }
                    case "administrative_area_level_1": {
                        String province = component.getShortName();
                        break;
                    }
                    case "country": {
                        String country = component.getName();
                        break;
                    }
                    case "postal_code":
                        String postalCode = component.getName();
                        break;

                }
            }
        }
        Log.i(TAG,"address"+address);
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