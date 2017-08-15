package com.Senior.Faff;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Model.Marker;
import com.Senior.Faff.Model.Party;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.utils.MapManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMap extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    private MapManager mManager;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private Marker marker;
    private Context context;
    private Location location;
    private LatLng myLocation;
    private Button buttonNone;
    private String getlocation;
    private TextView la,lo;
    protected Location mLastLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map);

        mManager = new MapManager(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        la = (TextView)findViewById(R.id.la);
        lo = (TextView)findViewById(R.id.lo);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        Bundle args = getIntent().getExtras();
        if(args != null){
            getlocation  = args.getString(Party.Column.Location);
        }
        Button add_place = (Button)findViewById(R.id.add_place);
        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent();
                if(getlocation != null) {
                    intent.putExtra(Restaurant.Column.Location, getlocation);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(AddMap.this,"Please select location",Toast.LENGTH_SHORT);
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
        //marker = new Marker("pee","sdfsdf","16.820,100.34");
        // mManager.addMarker(marker);

        // List<Marker> m = mManager.getMarker();
        mMap.setOnMyLocationButtonClickListener(this);
if(getlocation!=null) {
    String[] Eslat = getlocation.split(",");
    double a = Double.parseDouble(Eslat[0]);
    double b = Double.parseDouble(Eslat[1]);
    LatLng location = new LatLng(a, b);
    mMap.addMarker(new MarkerOptions()
            .title("This Place")
            .position(location)


    );
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13.0f));
}
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
                Toast.makeText(AddMap.this,getlocation,Toast.LENGTH_SHORT).show();
            }
        });


        enableMyLocation();



    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

        }
    }
    @Override
    public boolean onMyLocationButtonClick() {

        if (location != null) {
            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    11));
/*            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation);
            markerOptions.title("Your Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mCurrLocationMarker = mMap.addMarker(markerOptions);*/
            getlocation = myLocation.latitude + "," + myLocation.longitude;
            mMap.addMarker(new MarkerOptions()
                    .title("This Place")
                    .position(myLocation));
        }

        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    protected synchronized void CreateGoogleClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

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
    public void onConnected(Bundle bundle) {
        // Do something when connected with Google API Client

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction
            } else {
                /*      ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.LOCATION_FINE},
                        ACCESS_FINE_LOCATION);*/
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);  // ใช้สำหรัรับตำแหน่งแรกเลย ครั้งเดียว
                if (mLastLocation != null) {
                    getlocation = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();

                }


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
/*        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Toast.makeText(this,"On changed",Toast.LENGTH_SHORT);
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        lo.setText(String.valueOf(mLastLocation.getLongitude()));
        la.setText(String.valueOf(mLastLocation.getLatitude()));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
*//*        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*//*
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();

        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
}
