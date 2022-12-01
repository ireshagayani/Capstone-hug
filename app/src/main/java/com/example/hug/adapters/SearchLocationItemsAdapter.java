package com.example.hug.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hug.R;
import com.example.hug.models.ItemModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SearchLocationItemsAdapter extends RecyclerView.Adapter<SearchLocationItemsAdapter.ViewHolder>{

    private List<ItemModel> items;
    Context context;

    public SearchLocationItemsAdapter(Context ctx, List<ItemModel> items){
        this.context = ctx;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemCreatedDate.setText(item.getCreatedDateString());
        holder.itemQty.setText(item.getQty());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName, itemCreatedDate, itemQty;

        public ViewHolder(View view) {
            super(view);

            itemName = view.findViewById(R.id.search_location_item_list_itemName);
            itemCreatedDate = view.findViewById(R.id.search_location_item_list_createdDate);
            itemQty = view.findViewById(R.id.search_location_item_list_qty);
        }
    }
}
