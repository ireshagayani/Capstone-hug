package com.example.hug.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hug.R;
import com.example.hug.models.ItemModel;
import com.example.hug.ui.profile.ProfileAccountLocationViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.ViewHolder>{
    private List<ItemModel> items;
    Context context;
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

                TextView itemName = sheetview.findViewById(R.id.bottomsheet_profile_history_item_name);
                itemName.setText(item.getName());

                TextView itemAddress = sheetview.findViewById(R.id.bottomsheet_profile_history_item_address);
                itemAddress.setText(item.getName());

                TextView itemCreatedOn = sheetview.findViewById(R.id.bottomsheet_profile_history_item_createdOn);
                itemCreatedOn.setText(item.getCreatedDateString());

                TextView itemStatus = sheetview.findViewById(R.id.bottomsheet_profile_history_item_status);
                itemStatus.setText(item.getItemStatus());

                MaterialButton updateStatusBtn = sheetview.findViewById(R.id.profile_history_item_update_status_btn);
                updateStatusBtn.setText(item.getItemStatus() == "Available" ? "Not Available" : "Available");


                updateStatusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatus(item.getId(), item.getItemStatus() == "Available" ? "Not Available" : "Available");
                    }
                });

                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
            }
        });
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

    private void updateStatus(int id, String status){

    }
}
