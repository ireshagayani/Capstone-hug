package com.example.hug.ui.search;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hug.CustomDialogBox;
import com.example.hug.R;
import com.example.hug.databinding.FragmentMapsBinding;
import com.example.hug.locationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public FragmentMapsBinding binding;
    public List<String> dummyData0,dummyData1,dummyData2,dummyData3;
    private Marker wterloo,ktchener,cnestoga,kingstreet;
    List<locationModel> allLocationList;

    private SearchViewModel mViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater,container,false);

        getData();
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

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String api = "https://hug-conestoga.azurewebsites.net/api/Location";
        List<locationModel> allLocationList;


// Request a string response from the provided URL.
        Request stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i=0;i<array.length();i++)
                            {
                                JSONObject singleObj = array.getJSONObject(i);
                                locationModel singleModel =new locationModel(

                                );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("api", "onResponse: "+e.getMessage() );
                        }
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: " + response.substring(0,500));
                        Log.e("url", "onErrorResponse: "+response.toString() );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                Log.e("api", "onErrorResponse: "+error.getLocalizedMessage() );
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng waterloo = new LatLng(43.466667, -80.516670);
        this.wterloo = mMap.addMarker(new MarkerOptions().position(waterloo).title("Marker in waterloo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waterloo, 11));

        LatLng kitchener = new LatLng(43.4516, -80.4925);
        this.ktchener =  mMap.addMarker(new MarkerOptions().position(kitchener).title("Marker in kitchener"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kitchener));

        LatLng conestoga = new LatLng(43.3899, -80.4048);
        this.cnestoga =  mMap.addMarker(new MarkerOptions().position(conestoga).title("Marker in conestoga"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(conestoga));

        LatLng kingst = new LatLng(43.503149,-80.533586 );
        this.kingstreet = mMap.addMarker(new MarkerOptions().position(kingst).title("Marker in kingst"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kingst));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if(marker.equals(wterloo))
                {
                    CustomDialogBox dialogBox = new CustomDialogBox(getActivity(), dummyData0.get(0));
                    dialogBox.show();
                }
                else if(marker.equals(ktchener))
                {
                    CustomDialogBox dialogBox = new CustomDialogBox(getActivity(), dummyData1.get(0));
                    dialogBox.show();
                }
                else if(marker.equals(cnestoga))
                {
                    CustomDialogBox dialogBox = new CustomDialogBox(getActivity(), dummyData2.get(0));
                    dialogBox.show();
                }
                else if(marker.equals(kingstreet))
                {
                    CustomDialogBox dialogBox = new CustomDialogBox(getActivity(), dummyData3.get(0));
                    dialogBox.show();
                }
                else {
                    CustomDialogBox dialogBox = new CustomDialogBox(getActivity(), marker.getTitle());
                    dialogBox.show();
                }

                return true;
            }
        });
    }
}