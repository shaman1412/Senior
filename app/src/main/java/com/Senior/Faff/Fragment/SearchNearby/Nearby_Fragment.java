package com.Senior.Faff.Fragment.SearchNearby;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.Fragment.Home.top_resturant_MainFragment;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Restaurant_manager;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private ArrayList<Restaurant> re_list;
    private ListView listview;
    private  ArrayList<String> lalo;
    int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_nearby_, container, false);

        mcontext  = getContext();

        Restaurant model = new Restaurant();

       listview = (ListView)root.findViewById(R.id.listView12);
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Log.d("Myactivity", "omCreateview");



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
        Log.d("Myactivity", "onConnected");
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
            //showlist(listview,re_list,resId);

           // new getData().execute();
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
                     lalo =  locationStringFromLocation(mLastLocation);
                    new getData().execute(lalo.get(0),lalo.get(1));
                   /* Location target = new Location("Target");
                    target.setLatitude(37.5219983);
                    target.setLongitude(-122.184);
                    float d = mLastLocation.distanceTo(target);
                    //  d = location.distanceTo(target);
                    //   la.setText(String.valueOf(d));
                    if( mLastLocation!=null)
                        if( mLastLocation.distanceTo(target) > 1000) {
                            Toast.makeText(getContext(), "Hi", Toast.LENGTH_SHORT).show();
                        }
                   *//* if(location.distanceTo(target) < METERS_100) {
                        // bingo!
                    }*//*

                  *//*  lo.setText(String.valueOf(mLastLocation.getLongitude()));
                    la.setText(String.valueOf(mLastLocation.getLatitude()));*//*

                    showlist(listview,re_list,resId);
*/
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
    public ArrayList<Restaurant> calculatedistance(int de_distance,ArrayList<String> typeID,ArrayList<Restaurant> listRes){
        int distance;
        float latitude,longtitude;
        mLastLocation  = new Location("Target");
        mLastLocation.setLatitude(37.421998);
        mLastLocation.setLongitude(-122.084);
        ArrayList<Restaurant> res  = new ArrayList<>();
        for(int i =0; i<listRes.size(); i++){
          String[] position = listRes.get(i).getLocation().split(",");
            latitude = Float.parseFloat(position[0]);
            longtitude = Float.parseFloat(position[1]);
            Location target = new Location("Target");
            target.setLatitude(latitude);
            target.setLongitude(longtitude);
            distance  =  (int)mLastLocation.distanceTo(target);
            if(distance <= de_distance ){
                for(int j =0; j<typeID.size();j++) {
                    if(listRes.get(i).getTypefood() == typeID.get(j) )
                    res.add(listRes.get(i));
                }
            }
        }

        return res;
    }
    public void showlist(ListView listview,ArrayList<Restaurant> re_list,int[] resId){
        String h = "1,2";
        ArrayList<String> type_list = new ArrayList<>();
        String[] sp = h.split(",");
        for(int i = 0; i<sp.length ; i++){
            type_list.add(sp[i]);
        }
        Restaurant_manager res_manager = new Restaurant_manager(mcontext);
      //  re_list =  calculatedistance(900 ,type_list,res_manager.showAllRestaurant());
       // listview.setAdapter( new Customlistview_addvice_adapter(getContext(),0,re_list,resId));
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
    }
    private class getData extends AsyncTask<String, String, Restaurant[] > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected Restaurant[] doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_list/nearby/" + args[0] + "/" + args[1] ;
            try {
                URL url = new URL(url_api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                responseCode = connection.getResponseCode();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                Gson gson = new Gson();
                Restaurant[] userPro  =  gson.fromJson(result.toString(),  Restaurant[].class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(final Restaurant[] respro) {
            super.onPostExecute(respro);
            if (respro != null) {
                listview.setAdapter( new Customlistview_addvice_adapter(mcontext,0,respro,resId));
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        Intent intent = new Intent(mcontext, Show_RestaurantProfile.class);
                        intent.putExtra(Restaurant.Column.ResID,respro[position].getresId());
                        intent.putExtra(Restaurant.Column.UserID,respro[position].getUserID());
                        startActivity(intent);
                    }
                });


            }
            else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }
    public static ArrayList<String> locationStringFromLocation(final Location location) {
        ArrayList<String> lo = new ArrayList<>();
        lo.add(Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
        lo.add(Location.convert(location.getLongitude(), Location.FORMAT_DEGREES));
        return  lo;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.dialog_filter).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
