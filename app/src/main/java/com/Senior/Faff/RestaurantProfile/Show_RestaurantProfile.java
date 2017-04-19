package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Promotion.PromotionShow;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Show_RestaurantProfile extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = Show_RestaurantProfile.class.getSimpleName();
    private GoogleMap mMap;
    private Location location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private LatLng myLocation;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private String getlocation;
    private TextView name,telephone,period,description,address,type_food;
    private RecyclerView mRecyclerView;
    private ArrayList<String> favourite_type;
    private List_typeNodel list_adapter;
    private static Context mcontext;
    private String userid,resid;
    private Toolbar toolbar;
    private RatingBar rate;
    private TextView text_rate;
    private FloatingActionButton fab;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__restaurant_profile);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        id = getIntent().getExtras().getString("userid");

        mcontext = this;
        name = (TextView)findViewById(R.id.name);
        telephone = (TextView)findViewById(R.id.telephone);
        period = (TextView)findViewById(R.id.period);
        description = (TextView)findViewById(R.id.description);
        address = (TextView)findViewById(R.id.address);
        type_food = (TextView)findViewById(R.id.address);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rate = (RatingBar)findViewById(R.id.rate);
        text_rate = (TextView)findViewById(R.id.text_rate);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle args = getIntent().getExtras();
        if(args != null) {
            userid = args.getString(Restaurant.Column.UserID, null);
            resid = args.getString(Restaurant.Column.ResID,null);
        }
        if(userid != null){
            new getData().execute(resid);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show_RestaurantProfile.this,Edit_RestaurantProfile.class);
                intent.putExtra(Restaurant.Column.ResID,resid);
                intent.putExtra(Restaurant.Column.UserID,userid);
                startActivity(intent);
            }
        });
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                text_rate.setText(String.valueOf(new DecimalFormat("#.##").format(rating)));
                //ratingBar.getRating()
            }
        });


        GetUser getuser = new GetUser(new GetUser.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);
                UserProfile user = new Gson().fromJson(item.toString(), UserProfile.class);
                Log.i(TAG, " user is : "+user.toString());

                Bundle b = new Bundle();
                b.putString("id", id);
                b.putString("resid", resid);
                b.putString("username", user.getName());

                Comment_RestaurantFragment cmf = new Comment_RestaurantFragment();
                cmf.setArguments(b);
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.comment_restaurant_content, cmf).addToBackStack("frag_show_restaurant").commit();
            }
        });
        getuser.execute(id);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double a = -34;
        double b = 151;
            LatLng sydney = new LatLng(a,b);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10.0f));

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
            if(lola != null) {
                String[] pos = lola.split(",");
                myLocation = new LatLng(Double.parseDouble(pos[0]),
                        Double.parseDouble(pos[1]));
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
                address.setText("   "+respro.getAddress());
                telephone.setText(respro.getTelephone());
                description.setText("   "+respro.getDescription());
                period.setText(respro.getPeriod());
                rate.setRating(respro.getScore());
                text_rate.setText(String.valueOf(respro.getScore()));
                enableMyLocation(respro.getLocation(),respro.getRestaurantName());
                userid = respro.getUserID();
                resid =  respro.getresId();
                setTitle(respro.getRestaurantName());
                if(respro.getTypefood() != null){
                    String[] list = respro.getTypefood().split(",");
                    favourite_type = new ArrayList<String>();
                    for(int i = 0; i < list.length; i++){
                        favourite_type.add(list[i]);
                    }
                    list_adapter = new List_typeNodel(favourite_type, mcontext);
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

    private static class GetUser extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public Show_RestaurantProfile.GetUser.AsyncResponse delegate = null;

        public GetUser(Show_RestaurantProfile.GetUser.AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.i(TAG, "  params [0] is : "+params[0]);
                URL url = new URL("https://faff-1489402013619.appspot.com/user/"+params[0]);
                //URL url = new URL("http://localhost:8080/promotion_list");

                result = new Helper().getRequest(url.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
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
