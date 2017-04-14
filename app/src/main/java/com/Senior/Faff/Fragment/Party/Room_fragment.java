package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Restaurant_manager;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Room_fragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener {
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private Context context;
    private String name = "testuser";

    public Room_fragment() {
        // Required empty public constructor
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Context mcontext;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LatLng myLocation;
    private Location location;
    private ArrayList<Party> re_list;
    private ListView listview;
    private ArrayList<Party> party_list;
    private int gender,age;
    private String userid;
    int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_room_fragment, container, false);
    if(getArguments().getString(UserProfile.Column.UserID) != null) {
         userid = getArguments().getString(UserProfile.Column.UserID);
         gender = getArguments().getInt(UserProfile.Column.Gender);
         age = getArguments().getInt(UserProfile.Column.Age);

    }
        mcontext = getContext();

        Restaurant model = new Restaurant();

        listview = (ListView) root.findViewById(R.id.listView12);

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Log.d("Myactivity", "omCreateview");

  /*      DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                long count = dataSnapshot.child("All_Room").getChildrenCount();
                party_list = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.child("All_Room").getChildren()) {
                    Party post= postSnapshot.getValue(Party.class);
                    party_list.add(post);
                }

                Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Cant connect database",Toast.LENGTH_SHORT).show();
            }
        });*/
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
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                /*      ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.LOCATION_FINE},
                        ACCESS_FINE_LOCATION);*/
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
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
               // showlist(listview,party_list, resId);
               new getData().execute();
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);  // ใช้สำหรัรับตำแหน่งแรกเลย ครั้งเดียว
                if (mLastLocation != null) {

                    Location target = new Location("Target");
                    target.setLatitude(37.5219983);
                    target.setLongitude(-122.184);
                    float d = mLastLocation.distanceTo(target);
                    //  d = location.distanceTo(target);
                    //   la.setText(String.valueOf(d));
                    String a = "found location" + mLastLocation + "and" + target;
                    if (mLastLocation != null)
                        if (mLastLocation.distanceTo(target) > 1000) {
                           // Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
                        }
                   /* if(location.distanceTo(target) < METERS_100) {
                        // bingo!
                    }*/

                  /*  lo.setText(String.valueOf(mLastLocation.getLongitude()));
                    la.setText(String.valueOf(mLastLocation.getLatitude()));*/

                   // showlist(listview, party_list, resId);

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
        //mLastLocation = location;
        //showlist(listview, re_list, resId);
    }

    public ArrayList<Party> calculatedistance(int de_distance,   Map<String, String> rule_gender,Map<String, Integer> rule_age, ArrayList<Party> listRes) {
        int distance;
        int countcheck = 0;
        float latitude, longtitude;
        mLastLocation  = new Location("Target");
        mLastLocation.setLatitude(37.421998);
        mLastLocation.setLongitude(-122.084);
        ArrayList<Party> res = new ArrayList<>();
        for (int i = 0; i < listRes.size(); i++) {
            String[] position = listRes.get(i).getLocation().split(",");
            latitude = Float.parseFloat(position[0]);
            longtitude = Float.parseFloat(position[1]);
            Location target = new Location("Target");
            target.setLatitude(latitude);
            target.setLongitude(longtitude);
            distance = (int) mLastLocation.distanceTo(target);
            if (distance <= de_distance) {

                for (Map.Entry<String, String> check_rule : rule_gender.entrySet()) {
                    String check = listRes.get(i).getRule().get(check_rule.getKey());
                    String a = check_rule.getValue();
                    if (check != null) {
                        String[] sp = check.split(",");
                        for (int j = 0; j < sp.length; j++)
                            if (sp[j].equals(a)) {
                                countcheck++;
                            }
                    }
                }
                for (Map.Entry<String, Integer> check_rule : rule_age.entrySet()) {
                    String check = listRes.get(i).getRule().get(check_rule.getKey());
                    int a = check_rule.getValue();
                    if (check != null) {
                        int b = Integer.valueOf(check);
                        if (a < b) {
                            countcheck++;
                        }
                    }
                    if (countcheck == 2) {
                        countcheck = 0;
                        res.add(listRes.get(i));
                    }
                }
            }
        }
        return res;
    }


    public void showlist(ListView listview, ArrayList<Party> Pary_list, int[] resId, int gender, int age) {

        Map<String, String> rule_gender = new HashMap<>();
        if(gender == 0){
            rule_gender.put("gender","Female");
        }else{
            rule_gender.put("gender","Male");
        }
        Map<String, Integer> rule_age = new HashMap<>();
        rule_age.put("age", age);

        // Restaurant_manager res_manager = new Restaurant_manager(mcontext);
        if (Pary_list != null) {
            re_list = calculatedistance(900, rule_gender,rule_age, Pary_list);
            listview.setAdapter(new Customlistview_nearparty_adapter(getContext(), 0, re_list, resId));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                    if (position == 0) {
                        Toast.makeText(getContext(), "position 0", Toast.LENGTH_SHORT).show();
                    }
                    if (position == 1) {
                        Toast.makeText(getContext(), "position 1", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"Dont have party",Toast.LENGTH_SHORT);
        }

    }
    private class getData extends AsyncTask<Void, Integer, Void> {
        protected void onPreExecute()  {



        }

        protected Void doInBackground(Void... params)   {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mRootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot dataSnapshot){
                    long count = dataSnapshot.child("All_Room").getChildrenCount();
                    party_list = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.child("All_Room").getChildren()) {
                        Party post= postSnapshot.getValue(Party.class);
                        party_list.add(post);

                    }
                    showlist(listview, party_list, resId,gender,age);
                    //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(),"Cant connect database",Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        protected void onProgressUpdate(Integer... values) {


        }

        protected void onPostExecute(Void result)  {
           // Toast.makeText(getActivity(),"GEt dataaaa",Toast.LENGTH_SHORT).show();


        }
    }
}