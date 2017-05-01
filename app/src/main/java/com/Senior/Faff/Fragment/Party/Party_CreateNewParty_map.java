package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Add_map;
import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.CreatePartyManager;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Party_CreateNewParty_map extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private int MAP_REQUEST_CODE = 20;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private Location mLastLocation;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private Location location;
    private boolean mPermissionDenied = false;
    private Context context;
    private TextView la,lo;
    private int latitude,logtitude;
    private String position;
    private Context mcontext;
    private CreatePartyManager manager;
    private String user_id;
    //////////////////////
    private String roomid;
    private String name;
    private String description;
    private int people;
    private String rule;
    private String rule_age;
    private String rule_gender;
    private String address;
    private String appointment;
    private String telephone;
    private String createby;
    private String picture;
    private String getlocation;

    ////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party__create_new_party_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mcontext = this;
        la = (TextView)findViewById(R.id.la);
        lo = (TextView)findViewById(R.id.lo);
        Button add_location = (Button)findViewById(R.id.addlo);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if(location!=null) {
            la.setText(String.valueOf(location.getLatitude()));
            lo.setText(String.valueOf(location.getLongitude()));
        }
        Button btn = (Button)findViewById(R.id.addlo);
        if(getIntent().getStringExtra(Party.Column.RoomID) != null) {
            roomid = getIntent().getStringExtra(Party.Column.RoomID);
            name = getIntent().getStringExtra(Party.Column.Create_by_name);
            description = getIntent().getStringExtra(Party.Column.Description);
            people = getIntent().getIntExtra(Party.Column.People,0);
            rule = getIntent().getStringExtra(Party.Column.Rule);
            user_id = getIntent().getStringExtra(UserProfile.Column.UserID);
            rule_age = getIntent().getStringExtra(Party.Column.Rule_age);
            rule_gender = getIntent().getStringExtra(Party.Column.Rule_gender);
            address = getIntent().getStringExtra(Party.Column.Address);
            appointment = getIntent().getStringExtra(Party.Column.Appointment);
            telephone = getIntent().getStringExtra(Party.Column.Telephone);
            createby = getIntent().getStringExtra(UserProfile.Column.Name);
            picture = getIntent().getStringExtra(Party.Column.picture);
        }
        add_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(getlocation != null) {
                    manager = new CreatePartyManager(roomid,name,description,people,address,appointment,telephone,getlocation,rule_age, rule_gender,user_id,createby,picture);
                    manager.addroom();
                    Intent intent = new Intent(mcontext, Main2Activity.class);
                    intent.putExtra("userid",user_id);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(mcontext, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                // LatLngs.add(point);
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .title("This Place")
                        .position(point));
                getlocation = point.latitude + "," + point.longitude;
                Toast.makeText(Party_CreateNewParty_map.this,getlocation,Toast.LENGTH_SHORT).show();
            }
        });

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
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            if (locationAvailability != null) {


                LocationRequest locationRequest = new LocationRequest()  // ใช้สำหรับ onlicationchange ทำเรื่อยๆ
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(2000)
                        .setFastestInterval(2000);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null) {
                    myLocation = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                            11));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(myLocation);
                    markerOptions.title("This Place");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);
                    getlocation = location.getLatitude() + "," + location.getLongitude();
                }

            }

        }
    }
    @Override
    public boolean onMyLocationButtonClick() {

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            if (location != null) {
                myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        11));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(myLocation);
                markerOptions.title("This Place");
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                getlocation = location.getLatitude() + "," + location.getLongitude();
                //description.setText(String.valueOf(myLocation.latitude));
            }


        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Granted",Toast.LENGTH_SHORT);
            }
            else
                Toast.makeText(this,"Deny",Toast.LENGTH_SHORT);
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();


        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        enableMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
/*
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        Toast.makeText(this,"On changed",Toast.LENGTH_SHORT);
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position changed");
        position = location.getLatitude()+ "," + location.getLongitude();
        lo.setText(position);

        mCurrLocationMarker = mMap.addMarker(markerOptions);
*/

        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

}
