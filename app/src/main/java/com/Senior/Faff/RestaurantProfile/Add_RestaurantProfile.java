package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.UserProfile.List_type;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
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

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Add_RestaurantProfile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

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
    private EditText name, description, period, address, telephone;
    private Button picture;
    private Button next;
    private Context mcontext;
    private Toolbar toolbar;
    private String getlocation;
    private TextView showlocation;
    private Spinner type;
    private ArrayList<String> type_list;
    private boolean first = true;
    private List_type list_adapter;
    private RecyclerView mRecyclerView;
    private Restaurant restaurant;
    private String type_check;

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


        type = (Spinner) findViewById(R.id.favourite_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this, R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter_type);
        mcontext = this;
        //user_id = Integer.parseInt(get_user_id);
        //showlocation  = (TextView)findViewById(R.id.showlocation);
/*        if(getIntent().getStringExtra("Position") != null) {
            getlocation = getIntent().getStringExtra("Position");
            if (getlocation != null) {
                showlocation.setText(getlocation);
            }
        }*/
        if (getIntent().getStringExtra("userid") != null) {
            user_id = getIntent().getStringExtra("userid");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getString(R.string.Add_Restaurant_title));

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        period = (EditText) findViewById(R.id.period);
        address = (EditText) findViewById(R.id.address);
        telephone = (EditText) findViewById(R.id.telephone);
        type_list = new ArrayList<>();
        next = (Button) findViewById(R.id.next);
        picture = (Button) findViewById(R.id.picture);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_adapter != null) {
                    ArrayList<String> list_get = list_adapter.getlist();
                    if (list_get != null) {
                        for (int i = 0; i < list_get.size(); i++) {
                            if (i == 0) {
                                type_check = list_get.get(i);
                            } else {
                                type_check = type_check + list_get.get(i);
                            }
                            if (i != list_get.size() - 1) {
                                type_check = type_check + ",";
                            }
                        }
                    }
                }
                restaurant = new Restaurant(name.getText().toString(), address.getText().toString(), description.getText().toString(), period.getText().toString(), telephone.getText().toString(), user_id, type_check);
                long unixTime = System.currentTimeMillis() / 1000L;
                restaurant.setresId(user_id + String.valueOf(unixTime));
                restaurant.setLocation(getlocation);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                restaurant.setCreate_time(timestamp);
                new addrestaurant().execute(restaurant);
                //Intent intent = new Intent(getApplicationContext(), RestaurantMapsActivity.class);
                //intent.putExtra(UserProfile.Column.UserID,user_id);
                //startActivity(intent);
            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                String text = type.getSelectedItem().toString();
                boolean same = false;
                if (type_list != null) {
                    for (int i = 0; i < type_list.size(); i++) {
                        if (text.equals(type_list.get(i))) {
                            same = true;
                        }
                    }
                }
                if (same == false && first == false) {
                    type_list.add(text);
                    list_adapter = new List_type(type_list, mcontext);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);

                }
                if (first == true) {
                    first = false;
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

/*        myLocation = new LatLng(location.getLatitude(),
                location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                11));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLocation);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        getlocation = location.getLatitude() + "," + location.getLongitude();*/
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
        }else {
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
                    Toast.makeText(this," set getlocation ", Toast.LENGTH_SHORT).show();
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
    private class addrestaurant extends AsyncTask<Restaurant ,String , Restaurant > {

        int responseCode;
        HttpURLConnection connection;
        @Override
        protected Restaurant doInBackground(Restaurant... params) {

            try{

                JSONObject para = new JSONObject();
                para.put(Restaurant.Column.UserID,params[0].getUserID());
                para.put(Restaurant.Column.ResID, params[0].getresId());
                para.put(Restaurant.Column.RestaurantName, params[0].getRestaurantName());
                para.put(Restaurant.Column.Address, params[0].getAddress());
                para.put(Restaurant.Column.TypeFood, params[0].getTypefood());
                para.put(Restaurant.Column.Description, params[0].getDescription());
                para.put(Restaurant.Column.Telephone, params[0].getTelephone());
                para.put(Restaurant.Column.Period, params[0].getPeriod());
                para.put(Restaurant.Column.Location, params[0].getLocation());
                para.put(Restaurant.Column.CreateTime, params[0].getCreate_time());


                URL url = new URL("https://faff-1489402013619.appspot.com/res_profile/create");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                buffer.write(getPostDataString(para));
                buffer.flush();
                buffer.close();
                out.close();

                responseCode = connection.getResponseCode();

            }catch (Exception e){
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);

                return params[0];
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }
        }

        @Override
        protected void onPostExecute(Restaurant restaurant) {
            if(restaurant != null){

                Intent intent = new Intent(Add_RestaurantProfile.this, Main2Activity.class);
                intent.putExtra(Restaurant.Column.UserID,restaurant.getUserID());
                intent.putExtra(Restaurant.Column.ResID,restaurant.getresId());
                startActivity(intent);
                //Toast.makeText(mcontext,type_check,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(mcontext,"Fail",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


}

