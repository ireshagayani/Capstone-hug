package com.example.hug.ui.search;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hug.CustomDialogBox;
import com.example.hug.R;
import com.example.hug.adapters.ProfileHistoryAdapter;
import com.example.hug.adapters.SearchLocationItemsAdapter;
import com.example.hug.databinding.FragmentMapsBinding;
import com.example.hug.locationModel;
import com.example.hug.models.ItemModel;
import com.example.hug.models.LocationModel;
import com.example.hug.ui.APIClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public FragmentMapsBinding binding;
    public List<String> dummyData0,dummyData1,dummyData2,dummyData3;
    private List<LocationModel> locations;
    private Marker wterloo,ktchener,cnestoga,kingstreet;
    private BottomSheetDialog bottomSheetDialog;
    public ArrayList<LocationModel> allLocationList;
    private View locationDetailsView;

    private SearchViewModel mViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater,container,false);
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);



        //getData();
        dummyData0 = new ArrayList<String>();
        //Adding static data
        dummyData0.add("Address:Waterloo \nName : Charle's house\nAddress : 22 Cardill, Cresent\nCity : Waterloo\nProvince : Ontario\nPostal Code : N2L 3Y6\nPhone: 2265059167\nFood Type : Donuts\nQuantity : 5\nPickup Date: 21/12/2022");

        dummyData1 = new ArrayList<String>();
        //Adding static data
        dummyData1.add("Address:kitchener \nName : Nyzil's Home\nAddress : 143 Talbot st.\nCity : Kitchener\nProvince : Ontario\nPostal Code : N2L 3R2\nPhone: 5197231212\nFood Type : Crispy Chicken\nQuantity : 10\nPickup Date: 21/12/2022");
        dummyData2 = new ArrayList<String>();
        //Adding static data
        dummyData2.add("Address:Conestoga College  \nName :Inernational Food event \nCity : Kitchener\nProvince : Ontario\nPostal Code : N2L 3Y6\nPhone: 5165059167\nFood Type : burgers\nQuantity : 12\nPickup Date: 21/12/2022");
        dummyData3 = new ArrayList<String>();
        //Adding static data
        dummyData3.add("Address: KingST W \nName : Iresha's house\nAddress : 22 Victoriast, Cresent\nCity : Waterloo\nProvince : Ontario\nPostal Code : N2L 3Y6\nPhone: 5195059167\nFood Type : Fried Rice\nQuantity : 5\nPickup Date: 21/12/2022");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
         //       .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getLocations(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Search_Locations", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("locations", null);
        Type type = new TypeToken<ArrayList<LocationModel>>() {}.getType();
        allLocationList = gson.fromJson(json, type);
        updateMarkers();
    }

    private void updateMarkers(){
        if(allLocationList != null && allLocationList.size() > 0){
            int index = 0;
            for (LocationModel location: allLocationList) {
                if(!location.getItems().isEmpty()){
                    location.setFullAddress(location.getAddress() + " " + location.getCity() + " " + location.getPostalCode() + " " + location.getProvince() + " " + location.getCountry());
                    mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getContext(),location.getFullAddress())).title(String.valueOf(index)));
                }


                index++;
            }

            LatLng latLng = getLocationFromAddress(getContext(),allLocationList.get(allLocationList.size() - 1).getFullAddress());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.0f));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    openLocationDetailsBottomSheet(allLocationList.get(Integer.valueOf(marker.getTitle())));
                    return true;
                }
            });
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getLocations();
            }
        });
    }

     private void openLocationDetailsBottomSheet(LocationModel location){
         locationDetailsView = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_search_location_details, null);

         Log.e("BBB", location.getAddress());

         TextView locationName, locationAddress, locationCity, locationProvince, locationPostalCode, locationCountry, closeBtn;

         locationAddress = locationDetailsView.findViewById(R.id.search_location_address);
         locationAddress.setText(location.getAddress());

         locationCity = locationDetailsView.findViewById(R.id.search_location_city);
         locationCity.setText(location.getCity());

         locationPostalCode = locationDetailsView.findViewById(R.id.search_location_postalcode);
         locationPostalCode.setText(location.getPostalCode());

         locationProvince = locationDetailsView.findViewById(R.id.search_location_province);
         locationProvince.setText(location.getProvince());

//         locationCountry = sheetview.findViewById(R.id.search_location_country);
//         locationCountry.setText(location.getCountry());

         closeBtn = locationDetailsView.findViewById(R.id.search_location_details_close_btn);
         closeBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 bottomSheetDialog.hide();
             }
         });

         MaterialButton directionBtn = locationDetailsView.findViewById(R.id.search_direction_btn);
         directionBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         LatLng latLng = getLocationFromAddress(getContext(),location.getFullAddress());
//                         String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latLng.latitude, latLng.longitude);
                         String uri = "http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude+"&dirflg=w";
                         Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                         getContext().startActivity(intent);
                     }
                 }, 1000);
             }
         });

         MaterialButton callBtn = locationDetailsView.findViewById(R.id.search_call_btn);
         if(location.getPhone() != null){
             callBtn.setVisibility(View.VISIBLE);
             callBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             Intent callIntent = new Intent(Intent.ACTION_CALL);
                             callIntent.setData(Uri.parse("tel:" + location.getPhone()));
                             startActivity(callIntent);
                         }
                     }, 1000);
                 }
             });
         }
         else{
             callBtn.setVisibility(View.GONE);
         }

         setItems(location.getItems());

         bottomSheetDialog.setContentView(locationDetailsView);

         if(location.getItems() != null && location.getItems().size() <= 2 ){
             bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
             bottomSheetDialog.getBehavior().setPeekHeight(0);
         }

         bottomSheetDialog.show();
     }

     private void setItems(List<ItemModel> items){
        if(items.size() > 0){
            int index = 1;
            for (ItemModel i : items) {
                String qty = String.valueOf("Qty : " + i.getQty());
                String created = String.valueOf("Added On : " + i.getCreatedDateString());

                switch (index){
                    case 1:
                        MaterialCardView card1 = locationDetailsView.findViewById(R.id.item1);
                        TextView title1 = locationDetailsView.findViewById(R.id.item1_name);
                        title1.setText(i.getName());
                        TextView qty1 = locationDetailsView.findViewById(R.id.item1_qty);
                        qty1.setText(qty);
                        TextView created1 = locationDetailsView.findViewById(R.id.item1_created);
                        created1.setText(created);
                        card1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        MaterialCardView card2 = locationDetailsView.findViewById(R.id.item2);
                        TextView title2 = locationDetailsView.findViewById(R.id.item2_name);
                        title2.setText(i.getName());
                        TextView qty2 = locationDetailsView.findViewById(R.id.item2_qty);
                        qty2.setText(qty);
                        TextView created2 = locationDetailsView.findViewById(R.id.item2_created);
                        created2.setText(created);
                        card2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        MaterialCardView card3 = locationDetailsView.findViewById(R.id.item3);
                        TextView title3 = locationDetailsView.findViewById(R.id.item3_name);
                        title3.setText(i.getName());
                        TextView qty3 = locationDetailsView.findViewById(R.id.item3_qty);
                        qty3.setText(qty);
                        TextView created3 = locationDetailsView.findViewById(R.id.item3_created);
                        created3.setText(created);
                        card3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        MaterialCardView card4 = locationDetailsView.findViewById(R.id.item4);
                        TextView title4 = locationDetailsView.findViewById(R.id.item4_name);
                        title4.setText(i.getName());
                        TextView qty4 = locationDetailsView.findViewById(R.id.item4_qty);
                        qty4.setText(qty);
                        TextView created4 = locationDetailsView.findViewById(R.id.item4_created);
                        created4.setText(created);
                        card4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        MaterialCardView card5 = locationDetailsView.findViewById(R.id.item5);
                        TextView title5 = locationDetailsView.findViewById(R.id.item5_name);
                        title5.setText(i.getName());
                        TextView qty5 = locationDetailsView.findViewById(R.id.item5_qty);
                        qty5.setText(qty);
                        TextView created5 = locationDetailsView.findViewById(R.id.item5_created);
                        created5.setText(created);
                        card5.setVisibility(View.VISIBLE);
                        break;
                }
                index++;
            }
        }
     }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getLocations();
            }
        });
    }
}