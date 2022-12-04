package com.example.hug.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hug.GlobalVariables;
import com.example.hug.R;
import com.example.hug.adapters.ProfileHistoryAdapter;
import com.example.hug.models.ItemModel;
import com.example.hug.models.LocationModel;
import com.example.hug.ui.APIClient;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileHistoryFragment extends Fragment {

    private ProfileHistoryViewModel mViewModel;
    private View view;
    private CircularProgressIndicator progressIndicator;
    private RecyclerView historyList;
    private Integer userId = GlobalVariables.user_id;

    public static ProfileHistoryFragment newInstance() {
        return new ProfileHistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_history, container, false);
        progressIndicator = view.findViewById(R.id.profile_history_list_loading_bar);
        historyList = view.findViewById(R.id.profile_history_list_recyclerview);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileHistoryViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onResume() {
//        progressIndicator.setVisibility(View.VISIBLE);
        super.onResume();

        if(userId != null){
            loadItemsHistory(userId);
        }
        else{
            Toast.makeText(getContext(), "You have to Sign In or Create an account to view your profile!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadItemsHistory(int locationId) {
        List<ItemModel> result = new ArrayList<>();
        Call<List<ItemModel>> request = APIClient.getItemService().getAllItemsByLocationId(locationId);

        request.enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    List<ItemModel> result = response.body();
                    initializeView(result);
                } else {

                }
//                historyList.setVisibility(View.VISIBLE);
//                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                progressIndicator.setVisibility(View.GONE);
//                historyList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeView(List<ItemModel> items){
        RecyclerView historyRecyclerView = view.findViewById(R.id.profile_history_list_recyclerview);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setAdapter(new ProfileHistoryAdapter(getContext(),items));
    }
}