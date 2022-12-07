package com.example.hug.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hug.GlobalVariables;
import com.example.hug.R;
import com.example.hug.models.ItemModel;
import com.example.hug.models.LocationModel;
import com.example.hug.ui.APIClient;
import com.example.hug.ui.donate.DonateResponse;
import com.example.hug.ui.donate.DonateViewModel;
import com.example.hug.ui.donate.LocationViewModel;
import com.example.hug.ui.profile.ProfileAccountLocationViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.ViewHolder>{
    private List<ItemModel> items;
    Context context;
    List<Place.Field> fields;
    private BottomSheetDialog bottomSheetDialog;
    String updatedName , updatedDate , updatedLocation , updatedInstructions , updatedDateString ;
    Integer updatedQty , locationId , userId;
    Boolean itemStatus  = false;

    public ProfileHistoryAdapter(Context ctx, List<ItemModel> items){
        this.context = ctx;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.profile_history_list_row_item, viewGroup, false);

        bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetTheme);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ItemModel item = items.get(position);
        viewHolder.itemName.setText(item.getName());
        viewHolder.itemCreatedDate.setText(item.getCreatedDateString());
        viewHolder.itemStatus.setText(item.getItemStatus());

        viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View sheetview = LayoutInflater.from(view.getContext()).inflate(R.layout.bottomsheet_profile_history_item_status, null);

                TextInputEditText itemName = sheetview.findViewById(R.id.profile_history_item_title);
                itemName.setText(item.getName());

                TextInputEditText qty = sheetview.findViewById(R.id.profile_history_item_qty);
                qty.setText(String.valueOf(item.getQty()));

                TextInputEditText pickupLocation = sheetview.findViewById(R.id.profile_history_item_location);

                LocationModel location = item.getLocationModel();
                if(location != null){
                    pickupLocation.setText(location.getAddress()+" "+location.getCity()+" "+location.getProvince()+" "+location.getPostalCode());
                }


                if(!Places.isInitialized()){
                    Places.initialize( context, "AIzaSyC9MTMF1rU79kxeii_wV_urfiMnfTR0Dx8");
                }

                TextView closeBtn = sheetview.findViewById(R.id.profile_account_close_btn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.hide();
                    }
                });

                //create a new Places client instance
                PlacesClient placesClient = Places.createClient(context);
                pickupLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME,Place.Field.ADDRESS_COMPONENTS);

                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY , fields)
                                .build(context);
                        ((Activity) context).startActivityForResult(intent, 1);;
                    }
                });

                TextInputEditText pickupdate = sheetview.findViewById(R.id.profile_history_item_pickup_date);
                pickupdate.setText(item.getPickupDateTimeString());
                pickupdate.setInputType(InputType.TYPE_NULL);

                pickupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDateTimeDialog(pickupdate);
                    }
                });

                TextInputEditText pickupInstructions = sheetview.findViewById(R.id.profile_history_item_instructions);
                pickupInstructions.setText(item.getPickupInstruction());

                MaterialButton itemUpdateButton;
                itemUpdateButton = sheetview.findViewById(R.id.donate_create_btn);

                itemUpdateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatedName = itemName.getText().toString();
                        updatedQty = Integer.valueOf(qty.getText().toString());
                        updatedLocation = pickupLocation.getText().toString();
                        updatedDate = pickupdate.getText().toString() ;
                        updatedInstructions = pickupInstructions.getText().toString();
                        locationId = item.getLocationId();
                        updatedDateString = item.getCreatedDateString();

                        if(TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(qty.getText().toString()) || TextUtils.isEmpty(updatedLocation) || TextUtils.isEmpty(updatedDate)){

                            Toast.makeText(view.getContext(), "Enter all the required fields!", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            donateUpdate(item.getId(),view,item, position);
                        }
                    }
                });
                SwitchMaterial switchMaterial = sheetview.findViewById(R.id.profile_history_item_isAvaialble);
                switchMaterial.setChecked(item.getItemStatus().equals("Available") ? true : false);
                switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        itemStatus = b;
                    }
                });

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        });

        
    }

    private void donateUpdate(Integer id , View view,ItemModel itemModel, int selectedPosition) {
        //ItemModel itemModel = new ItemModel();
        //LocationViewModel locationViewModel;
        itemModel.setName(updatedName);
        itemModel.setQty(updatedQty);
        itemModel.setPickupDateTime(updatedDate);
        itemModel.setPickupInstruction(updatedInstructions);
        itemModel.setId(id);
        itemModel.setLocationId(locationId);
        itemModel.setPickupDateTimeString(updatedDateString);
        itemModel.setUserId(GlobalVariables.user_id);

        if(itemStatus == true){
            itemModel.setItemStatus("Available");
        }
        else{
            itemModel.setItemStatus("Not Available");
        }
        //locationViewModel = new LocationViewModel(GlobalVariables.user_id,GlobalVariables.user_id,addressLine1,addressLine2,city,province,country,postalCode);
        //donateViewModel.setLocation(locationViewModel);
       // itemModel.setCreatedBy(GlobalVariables.user_id);
        //itemModel.setUserId(GlobalVariables.user_id);

        Call<ItemModel> donateUpdateResponseCall = APIClient.getItemService().updateItem(id,itemModel);

        donateUpdateResponseCall.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                if (response.isSuccessful()) {
                    ItemModel item = response.body();
                    items.set(selectedPosition, item);
                    bottomSheetDialog.hide();
                    notifyItemChanged(selectedPosition);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            getLocationData();
                        }
                    });
                    Snackbar snackbar = Snackbar.make(view, "Donation Item updated Successfully!" , Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    Snackbar snackbar = Snackbar.make(view, "Item update Failed!" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                Toast.makeText(view.getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        donateUpdateResponseCall.enqueue(new Callback<DonateResponse>() {
//            @Override
//            public void onResponse(Call<DonateResponse> call, Response<DonateResponse> response) {
//                if(response.isSuccessful()){
//                    Snackbar snackbar = Snackbar.make(view, "Donation Item updated Successfully!" , Snackbar.LENGTH_LONG);
//                    snackbar.show();
////                    title_input.setText("");
////                    quantity_input.setText("");
////                    location_input.setText("");
////                    date_input.setText("");
////                    instructions_input.setText("");
//
////                    new Handler().post(new Runnable() {
////                        @Override
////                        public void run() {
////                            getLocationData();
////                        }
////                    });
//
//                }
//                else{
//                    Snackbar snackbar = Snackbar.make(view, "Item creation Failed!" , Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DonateResponse> call, Throwable t) {
//                Toast.makeText(view.getContext(), "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
    }

    private void getLocationData(){
        Call<List<LocationModel>> request =  APIClient.getLocationService().getAllLocations();
        request.enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, retrofit2.Response<List<LocationModel>> response) {
                if (response.isSuccessful()) {
                    List<LocationModel> locations = response.body();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("Search_Locations", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(locations);
                    editor.putString("locations", json);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(context, "Unable to load data. " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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
                new TimePickerDialog(context,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(context,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName, itemCreatedDate, itemStatus;
        private final MaterialCardView wrapper;

        public ViewHolder(View view) {
            super(view);
            wrapper = view.findViewById(R.id.profile_list_row_item_wrapper);
            itemName = view.findViewById(R.id.profile_list_row_item_name);
            itemCreatedDate = view.findViewById(R.id.profile_list_row_item_created_date);
            itemStatus = view.findViewById(R.id.profile_list_row_status);
        }
    }

}
