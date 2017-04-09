package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Add_RestaurantProfile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private GoogleApiClient googleApiClient;
    private Location location;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private GoogleMap mMap;
    private String get_user_id;
    private String user_id;
    private int type_food;
    private EditText name,detail,picture,description;
    private Button next;
    private Context mcontext;
    private Toolbar toolbar;
    private String getlocation;
    private TextView showlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res_respro);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        String value[] = new String[]{"Food on order", "Steak", "Pizza", "Noodle"};
        Spinner spinner = (Spinner) findViewById(R.id.type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, value);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        mcontext = this;
        //user_id = Integer.parseInt(get_user_id);
        //showlocation  = (TextView)findViewById(R.id.showlocation);
/*        if(getIntent().getStringExtra("Position") != null) {
            getlocation = getIntent().getStringExtra("Position");
            if (getlocation != null) {
                showlocation.setText(getlocation);
            }
        }*/
        if(getIntent().getStringExtra("userid") != null) {
            user_id = getIntent().getStringExtra("userid");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getString(R.string.Add_Restaurant_title));

        name = (EditText) findViewById(R.id.addName);
        detail = (EditText) findViewById(R.id.addDetail);

        next = (Button) findViewById(R.id.next);
        picture = (EditText) findViewById(R.id.addPicture);
        description = (EditText)findViewById(R.id.description);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantMapsActivity.class);
                intent.putExtra("userid",user_id);
                startActivity(intent);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

                switch (pos) {
                    case 0:
                        type_food = 0;
                        Toast.makeText(Add_RestaurantProfile.this,"0",Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        type_food = 1;
                        Toast.makeText(Add_RestaurantProfile.this,"1",Toast.LENGTH_SHORT);
                        break;
                    case 2:
                        type_food = 2;
                        Toast.makeText(Add_RestaurantProfile.this,"2",Toast.LENGTH_SHORT);
                        break;
                    case 3:
                        type_food = 3;
                        break;

                }

               /* Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
/*        Button addre = (Button) findViewById(R.id.add);
        addre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant(name.getText().toString(), picture.getText().toString(), getlocation, detail.getText().toString(), 0, user_id, type_food);
                Restaurant_manager manager = new Restaurant_manager(mcontext);
                long result = manager.AddRestaurant(restaurant);
                if (result == -1) {
                    Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, Main2Activity.class);
                    intent.putExtra("userid",user_id);
                    startActivity(intent);

                }
            }
        });*/

    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                    // if this doesn't work as desired, another possibility is to call `finish()` here.
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
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

    @Override
    public boolean onMyLocationButtonClick() {
        if (location != null) {
            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    11));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            description.setText(String.valueOf(myLocation.latitude));

        }


        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
/*        LatLng sydney = new LatLng(37.62,-122.284);
        mMap.addMarker(new MarkerOptions().position(sydney).title("PPAP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
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

            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    11));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mCurrLocationMarker = mMap.addMarker(markerOptions);


        }
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

}

