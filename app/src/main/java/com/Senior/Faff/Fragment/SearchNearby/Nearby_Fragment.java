package com.Senior.Faff.Fragment.SearchNearby;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Restaurant_manager;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby_Fragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener{


    public Nearby_Fragment() {
        // Required empty public constructor
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Context mcontext;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LatLng myLocation;
    private Location location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_nearby_, container, false);
        int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};
        mcontext  = getContext();
        Restaurant_manager res_manager = new Restaurant_manager(mcontext);
        Restaurant model = new Restaurant();
        ArrayList<Restaurant> re_list = new ArrayList<>();
        re_list = res_manager.showAllRestaurant();
        ListView listview = (ListView)root.findViewById(R.id.listView12);
        listview.setAdapter( new Customlistview_addvice_adapter(getContext(),0,re_list,resId));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                if(position == 0){
                    Toast.makeText(getContext(),"position 0",Toast.LENGTH_SHORT).show();
                }
                if(position== 1){
                    Toast.makeText(getContext(),"position 1",Toast.LENGTH_SHORT).show();
                }
            }
        });
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();





        return root;
    }
    @Override
    public void onStart() {
        super.onStart();

        // Connect to Google API Client
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            // Disconnect Google API Client if available and connected
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
               new AlertDialog.Builder(mcontext)
                       .setTitle("")
                       .setMessage("")
                       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                           }
                       })
                       .create()
                       .show();
            } else {
                /*      ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.LOCATION_FINE},
                        ACCESS_FINE_LOCATION);*/
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else {
            //createLocationRequest();

            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            if (locationAvailability != null) {


                LocationRequest locationRequest = new LocationRequest()  // ใช้สำหรับ onlicationchange ทำเรื่อยๆ
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(2000)
                        .setFastestInterval(2000);
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);  // ใช้สำหรัรับตำแหน่งแรกเลย ครั้งเดียว
                if (mLastLocation != null) {

                    Location target = new Location("Target");
                    target.setLatitude(37.5219983);
                    target.setLongitude(-122.184);
                    float d;
                    //  d = location.distanceTo(target);
                    //   la.setText(String.valueOf(d));
                    if(location!=null)
                        if(location.distanceTo(target) < 1000) {
                            Toast.makeText(getContext(), "Hi", Toast.LENGTH_SHORT).show();
                        }
                   /* if(location.distanceTo(target) < METERS_100) {
                        // bingo!
                    }*/

                  /*  lo.setText(String.valueOf(mLastLocation.getLongitude()));
                    la.setText(String.valueOf(mLastLocation.getLatitude()));*/

                }

                // LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }



        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
