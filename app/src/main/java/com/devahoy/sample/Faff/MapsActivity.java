package com.devahoy.sample.Faff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import android.Manifest;
import com.devahoy.sample.Faff.model.Marker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.devahoy.sample.Faff.utils.PermissionUtils;
import java.util.List;

import com.devahoy.sample.Faff.utils.MapManager;
public class MapsActivity extends AppCompatActivity implements  OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private MapManager mManager;
    private Marker marker;
    private Context context;
    private Location location;
    private LatLng myLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mManager = new MapManager(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button buttonNone = (Button)findViewById(R.id.buttonNone);
        buttonNone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
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
/*        marker = new Marker("pee","sdfsdf","-34, 151");
        mManager.addMarker(marker);*/

        List<Marker> m = mManager.getMarker();
        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();
        double a = -34;
        double b = 151;
            LatLng sydney = new LatLng(a,b);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10.0f));


   /*     for(int i = 0 ;i < m.size();i++){
            String[] Eslat = m.get(i).getMarkerPostion().split(",");
            double a = Double.parseDouble(Eslat[0]);
            double b = Double.parseDouble(Eslat[1]);
            LatLng location = new LatLng(a,b);
            mMap.addMarker(new MarkerOptions()
                    .title(m.get(i).getMarkerTitle())
                    .snippet(m.get(i).getMarkerSnippet())
                    .position(location)

            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17.0f));

        }*/



    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
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
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                11));
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
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

}
