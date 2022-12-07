package com.example.hug.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hug.R;
import com.example.hug.models.ItemModel;
import com.example.hug.ui.profile.ProfileAccountLocationViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.ViewHolder>{
    private List<ItemModel> items;
    Context context;
    List<Place.Field> fields;
    private BottomSheetDialog bottomSheetDialog;

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
                pickupLocation.setText(item.getName());
                if(!Places.isInitialized()){
                    Places.initialize( context, "AIzaSyC9MTMF1rU79kxeii_wV_urfiMnfTR0Dx8");
                }

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

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
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
