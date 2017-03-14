package com.Senior.Faff;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import android.location.Location;
import android.widget.TextView;

public class LocationProvider extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private GoogleApiClient googleApiClient;
    private TextView textView,a,b;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private static final int REQUEST_LOCATION = 2;
    protected Location mLastLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_provider);
        textView = (TextView) findViewById(R.id.a);
        a = (TextView)findViewById(R.id.s);
        b = (TextView) findViewById(R.id.d);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction
            } else {
                /*      ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.LOCATION_FINE},
                        ACCESS_FINE_LOCATION);*/
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else {
            //createLocationRequest();

            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            if (locationAvailability != null) {

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        googleApiClient);
                if (mLastLocation != null) {
                    a.setText(String.valueOf(mLastLocation.getLatitude()));
                    b.setText(String.valueOf(mLastLocation.getLongitude()));
                }
            }

            if (locationAvailability.isLocationAvailable()) {
                // Call Location Services

                LocationRequest locationRequest = new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(5000);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                // Do something when Location Provider not available
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do something when Google API Client connection was suspended

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do something when Google API Client connection failed

    }

    @Override
    public void onLocationChanged(Location location) {
        // Do something when got new current location
        Double a = location.getLatitude();
        Double b = location.getLongitude();

        textView.setText("Latitude : " + location.getLatitude() + "\n" +
                "Longitude : " + location.getLongitude());
    }
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());

    }
/*
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }
*/





}
