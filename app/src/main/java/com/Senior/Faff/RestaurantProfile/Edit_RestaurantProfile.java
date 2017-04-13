package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_type;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class Edit_RestaurantProfile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener{

    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private EditText name,description,period,address,telephone;
    private Toolbar toolbar;
    private String user_id;
    private String resid;
    private ArrayList<String> type_list;
    private Button next;
    private Button picture;
    private Spinner type;
    private Context mcontext;
    private List_type list_adapter;
    private RecyclerView mRecyclerView;
    private  Restaurant restaurant;
    private String type_check;
    private String getlocation;
    private boolean first = true;
    private GoogleMap mMap;
    private Location location;
    private ArrayList<String> favourite_type;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__restaurant_profile);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        type = (Spinner) findViewById(R.id.favourite_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter_type);
        mcontext = this;

        Bundle arg = getIntent().getExtras();
        if(arg != null) {
            user_id = arg.getString(Restaurant.Column.UserID);
            resid = arg.getString(Restaurant.Column.ResID);

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getString(R.string.Add_Restaurant_title));
        name = (EditText) findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        period = (EditText)findViewById(R.id.period);
        address = (EditText)findViewById(R.id.address);
        telephone = (EditText)findViewById(R.id.telephone);
        type_list = new ArrayList<>();
        next = (Button) findViewById(R.id.next);
        picture = (Button) findViewById(R.id.picture);
        new getData().execute(resid);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String>  list_get  = list_adapter.getlist();
                if(list_get != null) {
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
                restaurant = new Restaurant(name.getText().toString(),address.getText().toString(),description.getText().toString(),period.getText().toString(),telephone.getText().toString(),user_id, type_check);
                restaurant.setresId(user_id + "pee");
                restaurant.setLocation(getlocation);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                restaurant.setCreate_time(timestamp);
                new updaterestaurant().execute(restaurant);
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
                if(type_list != null) {
                    for (int i = 0; i < type_list.size(); i++) {
                        if (text.equals(type_list.get(i))) {
                            same = true;
                        }
                    }
                }
                if(same == false && first == false) {
                    type_list.add(text);
                    list_adapter = new List_type(type_list, mcontext);
                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);

                }
                if(first == true){
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
    }
    private void enableMyLocation(String lola,String res_name) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            //mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            String[] pos = lola.split(",");
            myLocation = new LatLng(Double.parseDouble(pos[0]),
                    Double.parseDouble(pos[0]));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    11));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation);
            markerOptions.title(res_name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            //    getlocation = location.getLatitude() + ","  + location.getLongitude();


        }
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
        }

        return false;
    }

    private class updaterestaurant extends AsyncTask<Restaurant ,String , Restaurant > {

        int responseCode;
        HttpURLConnection connection;
        @Override
        protected Restaurant doInBackground(Restaurant... params) {

            try{

                JSONObject para = new JSONObject();
                para.put(Restaurant.Column.RestaurantName, params[0].getRestaurantName());
                para.put(Restaurant.Column.Address, params[0].getAddress());
                para.put(Restaurant.Column.TypeFood, params[0].getTypefood());
                para.put(Restaurant.Column.Description, params[0].getDescription());
                para.put(Restaurant.Column.Telephone, params[0].getTelephone());
                para.put(Restaurant.Column.Period, params[0].getPeriod());
                para.put(Restaurant.Column.Location, params[0].getLocation());


                String url_api = "https://faff-1489402013619.appspot.com/res_profile/update/" + params[0];
                URL url = new URL(url_api);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("PUT");
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

                Intent intent = new Intent(Edit_RestaurantProfile.this, Main2Activity.class);
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
    private class getData extends AsyncTask<String, String, Restaurant >{

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected Restaurant doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_profile/" + args[0];
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
                Restaurant userPro  =  gson.fromJson(result.toString(),  Restaurant.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(Restaurant respro) {
            super.onPostExecute(respro);
            if (respro != null) {
                name.setText(respro.getRestaurantName());
                address.setText(respro.getAddress());
                telephone.setText(respro.getTelephone());
                description.setText(respro.getDescription());
                period.setText(respro.getPeriod());
                enableMyLocation(respro.getLocation(),respro.getRestaurantName());

                if(respro.getTypefood() != null){
                    String[] list = respro.getTypefood().split(",");
                    favourite_type = new ArrayList<String>();
                    for(int i = 0; i < list.length; i++){
                        favourite_type.add(list[i]);
                    }
                    list_adapter = new List_type(favourite_type, mcontext);
                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);
                }

            }
            else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

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
}
