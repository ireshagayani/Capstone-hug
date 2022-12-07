package com.example.hug.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hug.GlobalVariables;
import com.example.hug.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private Button sheetButton;
    private BottomSheetDialog bottomSheetDialog;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ProfileAccountFragment profileAccountFragment;
    private ProfileHistoryFragment profileHistoryFragment;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

       //Toast.makeText(getContext(), "signed in as :"+GlobalVariables.user_id, Toast.LENGTH_SHORT).show();

        viewPager = view.findViewById(R.id.profile_tab_viewpager);
        tabLayout = view.findViewById(R.id.profile_TabLayout);

        profileAccountFragment = new ProfileAccountFragment();
        profileHistoryFragment = new ProfileHistoryFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(profileAccountFragment, "Account");
        viewPagerAdapter.addFragment(profileHistoryFragment, "History");

        if(GlobalVariables.user_id != null){
            viewPager.setAdapter(viewPagerAdapter);
        }

        else{
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
            alertDialogBuilder.setTitle("VIEW PROFILE !");
            alertDialogBuilder.setMessage("You have to Sign In or Create an account to view Profile!");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GlobalVariables.profileNav = true;
                    navController.navigate(R.id.navigation_login);

                }
            });
            alertDialogBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialogBuilder.show();
        }


        //Toast.makeText(getContext(), "userId:"+GlobalVariables.user_id, Toast.LENGTH_SHORT).show();
//        sheetButton = view.findViewById(R.id.profile_open_sheet_btn);

//        sheetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetTheme);
//                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_profile_account, null);
//
//                sheetview.findViewById(R.id.profile_account_close_btn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        bottomSheetDialog.hide();
//                    }
//                });
//
//
//                sheetview.findViewById(R.id.profile_account_update_btn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//
//
//                bottomSheetDialog.setContentView(sheetview);
//                bottomSheetDialog.show();
//            }
//        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> tabItems = new ArrayList<Fragment>();
        private List<String> tabTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            //Toast.makeText(getContext(), "title"+title, Toast.LENGTH_SHORT).show();
            tabItems.add(fragment);
            tabTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return tabItems.get(position);
        }

        @Override
        public int getCount() {
            return tabItems.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);
        }
    }

}