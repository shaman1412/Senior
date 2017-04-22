package com.Senior.Faff.Fragment.Party;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.CreatePartyManager;

import com.Senior.Faff.R;
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

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Party_CreateNewParty extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private GoogleApiClient googleApiClient;
    private Location location;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private GoogleMap mMap;
    private EditText name,description,people,address,appointment,telephone;
    private Spinner spinner1;
    private String getlocation;
    private Button add_rule,create,rule;
    private CreatePartyManager manager;
    private String roomid,userid;
    private Party party;
    private ArrayList<String> rule_gender;
    private String rule_age;
    private String createby;
    private String getrule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party__create_new_party);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_party);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        people = (EditText)findViewById(R.id.people);
        address = (EditText)findViewById(R.id.address);
        appointment = (EditText)findViewById(R.id.period);
        telephone = (EditText)findViewById(R.id.telephone);
        add_rule = (Button)findViewById(R.id.rule);
        create = (Button)findViewById(R.id.next);
        rule_gender = new ArrayList<>();
        Bundle args = getIntent().getExtras();
        if(args != null){
          userid  = args.getString(UserProfile.Column.UserID);
            createby = args.getString(UserProfile.Column.Name);

        }
        long unixTime = System.currentTimeMillis() / 1000L;
        roomid = userid+ "ppap" + String.valueOf(unixTime);

        add_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Party_CreateNewParty.this,Party_CreateNewParty_map.class);
                if(rule_gender != null) {
                    for (int i = 0; i < rule_gender.size(); i++) {
                        if (i == 0) {
                            getrule = rule_gender.get(i);
                        } else
                            getrule = getrule + rule_gender.get(i);
                        if (i != rule_gender.size() - 1) {
                            getrule = getrule + ",";
                        }
                    }
                }
                intent.putExtra(Party.Column.RoomID, roomid );
                intent.putExtra(Party.Column.Create_by_name, name.getText().toString());
                intent.putExtra(Party.Column.Description,description.getText().toString());
                intent.putExtra(Party.Column.People, Integer.parseInt(people.getText().toString()));
                intent.putExtra(Party.Column.Address,address.getText().toString());
                intent.putExtra(Party.Column.Appointment,appointment.getText().toString());
                intent.putExtra(Party.Column.Telephone,telephone.getText().toString());
                intent.putExtra(Party.Column.Rule_gender,getrule);
                intent.putExtra(Party.Column.Rule_age,rule_age);
                intent.putExtra(UserProfile.Column.UserID,userid);
                intent.putExtra(UserProfile.Column.Name,createby);
                startActivity(intent);
            }
        });


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
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Party_CreateNewParty.this);
        View promptView = layoutInflater.inflate(R.layout.rule_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Party_CreateNewParty.this);
        alertDialogBuilder.setView(promptView);
        final  AlertDialog alert = alertDialogBuilder.create();
        // final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
      /*  alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Main2Activity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/

        Button btn_1 = (Button)promptView.findViewById(R.id.btn1);
        Button btn_2 = (Button) promptView.findViewById(R.id.btn2);
     final  CheckBox chk1 = (CheckBox)promptView.findViewById(R.id.chk1);
     final  CheckBox chk2 = (CheckBox)promptView.findViewById(R.id.chk2);
      final  EditText age = (EditText)promptView.findViewById(R.id.age);



        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.cancel();

            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chk1.isChecked()){
                    rule_gender.add("Female");
                }
                if(chk2.isChecked()){
                    rule_gender.add("Male");
                }
                if(!(chk1.isChecked() || chk2.isChecked()))
                {
                    rule_gender.add("Male");
                    rule_gender.add("Female");
                }
                if(age.getText().toString() != null) {
                    rule_age = age.getText().toString();
                }
                alert.cancel();
            }
        });
        // create an alert dialog

        alert.show();

    }
    public void kk(CheckBox chk1, CheckBox chk2){


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        enableMyLocation();
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        11));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(myLocation);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                getlocation = location.getLatitude() + "," + location.getLongitude();
                //description.setText(String.valueOf(myLocation.latitude));
                Toast.makeText(this, "getlocation = get ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "getlocation = null ", Toast.LENGTH_SHORT).show();
            }
        }


        // Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
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

                Toast.makeText(this, "set location ", Toast.LENGTH_SHORT).show();
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
                    getlocation = location.getLatitude() + "," + location.getLongitude();
                    Toast.makeText(this, " set getlocation ", Toast.LENGTH_SHORT).show();
                }

            }
            Toast.makeText(this, "cant set location", Toast.LENGTH_SHORT).show();

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
