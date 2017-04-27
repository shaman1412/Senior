package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
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

import com.Senior.Faff.Add_map;
import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.UserProfile.InsertUserProfileRecyclerView;
import com.Senior.Faff.UserProfile.List_type;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
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

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Add_RestaurantProfile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri

    public static int image_count = 0;                                    //number of images
    private final int MAP_REQUEST_CODE = 20;
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
    private Button next, upload;
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
    private String send_location;
    private Add_RestaurantProfile_RecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res_respro);
        setTitle("Add Restaurant Profile");
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
        upload = (Button) findViewById(R.id.upload_button);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //sdintent.setType("image/*");
                startActivityForResult(sdintent, request_code);
            }
        });

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

                String img_path_tmp = new Gson().toJson(imgPath);
                restaurant.setPicture(img_path_tmp);

                Add_RestaurantProfile.AddRestProfile add_pro = new Add_RestaurantProfile.AddRestProfile();
                add_pro.execute(restaurant);


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
      //  send_location = location.getLatitude() + "," + location.getLongitude();
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(Add_RestaurantProfile.this, Add_map.class);
                intent.putExtra(Party.Column.Location, send_location);
                startActivityForResult(intent, MAP_REQUEST_CODE);
            }
        });
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
                    send_location = location.getLatitude() + "," + location.getLongitude();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == request_code && data != null) {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cur = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                if (cur == null) imgPath.add(null);
                else {
                    int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cur.moveToFirst();
                    imgPath.add(cur.getString(column_index));
                    cur.close();
                }
                bmap.add(BitmapFactory.decodeFile(imgPath.get(image_count)));
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new Add_RestaurantProfile_RecyclerView(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            }else if(requestCode == MAP_REQUEST_CODE) {
                getlocation = data.getStringExtra(Restaurant.Column.Location);
               String[] split = getlocation.split(",");
               LatLng lola =  new LatLng(Double.parseDouble(split[0]),Double.parseDouble(split[1]));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lola,11));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(lola);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                Toast.makeText(Add_RestaurantProfile.this,getlocation,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AddRestProfile extends AsyncTask<Restaurant, String, String> {

        private ArrayList<String> imgPath = new ArrayList<>();
        String result = "";

        @Override
        protected String doInBackground(Restaurant... params) {
            try {
                Map<String, String> paras = new HashMap<>();
                paras.put(Restaurant.Column.UserID, params[0].getUserID());
                paras.put(Restaurant.Column.ResID, params[0].getresId());
                paras.put(Restaurant.Column.RestaurantName, params[0].getRestaurantName());
                paras.put(Restaurant.Column.Address, params[0].getAddress());
                paras.put(Restaurant.Column.TypeFood, params[0].getTypefood());
                paras.put(Restaurant.Column.Description, params[0].getDescription());
                paras.put(Restaurant.Column.Telephone, params[0].getTelephone());
                paras.put(Restaurant.Column.Period, params[0].getPeriod());
                paras.put(Restaurant.Column.Location, params[0].getLocation());
                paras.put(Restaurant.Column.CreateTime, params[0].getCreate_time().toString());

                String img_path_tmp = params[0].getPicture();
                Add_RestaurantProfile.AddRestProfile.this.imgPath = new Gson().fromJson(img_path_tmp, ArrayList.class);

                URL url = new URL("https://faff-1489402013619.appspot.com/res_profile/create");

                result = new Helper().multipartRequest(url.toString(),paras, Add_RestaurantProfile.AddRestProfile.this.imgPath, "image", "image/jpeg");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                finish();
                Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mcontext, "Fail on retrieve result", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

