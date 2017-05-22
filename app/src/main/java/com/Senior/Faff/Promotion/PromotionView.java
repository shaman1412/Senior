package com.Senior.Faff.Promotion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.Promotion_MapsActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.Senior.Faff.utils.LoadingFragment;
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

import java.net.HttpURLConnection;
import java.net.URL;

public class PromotionView extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = PromotionView.class.getSimpleName();

    //    ListView mListPromotion;
    private String send_location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private LatLng myLocation;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private String getlocation;
    private Location location;
    static Context mContext;
    private Toolbar toolbar;
    private String userid;
    private GoogleMap mMap;
    private Promotion data;

    private static FrameLayout loading;
    private static LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_view);

        loading = (FrameLayout) findViewById(R.id.loading);
        loading.bringToFront();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = this;
        Intent i = getIntent();
       userid =  i.getExtras().getString(UserProfile.Column.UserID);
       final String id = i.getExtras().getString("id");

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                Log.i(TAG, "  item is : "+item.toString());

                data = new Gson().fromJson(item.toString(), Promotion.class);
                String[] arr_url = item.getString("promotionpictureurl").split(",");

                TextView textView1 = (TextView) findViewById(R.id.pro_name);
                textView1.setText(data.getTitle());

                TextView textView2 = (TextView) findViewById(R.id.startDate_text);
                textView2.setText(data.getStartDate());

                TextView textView3 = (TextView) findViewById(R.id.endDate_text);
                textView3.setText(data.getEndDate());

                TextView textView4 = (TextView) findViewById(R.id.pro_detail_text);
                textView4.setText(data.getPromotionDetail());


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.promo_image);
                PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(mContext, arr_url);
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);

                enableMyLocation(data.getGoogleMapLink(),data.getTitle());

                TextView edit_pro =(TextView)findViewById(R.id.edit_pro);
                edit_pro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,Edit_promotion.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });
                ////////////////////////////////////////////// Delete ////////////////////////////////////////////////
                TextView delete_pro = (TextView)findViewById(R.id.delete_pro);
                delete_pro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new delete().execute(id);
                    }
                });

                if(userid.equals(data.getUserid())){
                    delete_pro.setVisibility(View.VISIBLE);
                    edit_pro.setVisibility(View.VISIBLE);
                }
                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }

            }
        });
        lsp.execute(id);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double a = -34;
        double b = 151;
        LatLng sydney = new LatLng(a,b);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(PromotionView.this, Promotion_MapsActivity.class);
                intent.putExtra(Promotion.Column.Location,data.getGoogleMapLink());
                intent.putExtra(Promotion.Column.Title, data.getTitle());
                startActivity(intent);
            }
        });
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
                send_location = lola;
                String[] pos = lola.split(",");
                try {
                    myLocation = new LatLng(Double.parseDouble(pos[0]),
                            Double.parseDouble(pos[1]));
                } catch (NumberFormatException e) {
                    myLocation = new LatLng(0, 0);
                }
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

    private static class ListPromotion extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public PromotionView.ListPromotion.AsyncResponse delegate = null;

        public ListPromotion(PromotionView.ListPromotion.AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            showLoading();
            try {
                loadingFragment = new LoadingFragment();
                ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/"+params[0]);
                //URL url = new URL("http://localhost:8080/promotion_list");

                Helper hp = new Helper();
                hp.setRequest_method("GET");
                result = hp.getRequest(url.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
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
    public class delete extends AsyncTask<String , Void, Boolean>{
        int responseCode;
        HttpURLConnection connection;
        @Override
        protected Boolean doInBackground(String... params) {
            try{
                JSONObject para = new JSONObject();
                String url_api = "https://faff-1489402013619.appspot.com/restaurant_promotion/promotionid/del/" + params[0];
                URL url = new URL(url_api);
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded" );
                connection.setRequestMethod("DELETE");
                connection.connect();

                responseCode = connection.getResponseCode();

            }catch (Exception e){
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                return true;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(mContext,"Restaurant deleted",Toast.LENGTH_SHORT);
                Intent intent = new Intent(mContext, Main2Activity.class);
                //intent.putExtra(UserProfile.Column.UserID,userid);
                startActivity(intent);
            }else{
                Toast.makeText(mContext,"Cant Delete",Toast.LENGTH_SHORT);
            }
        }
    }

    public static void showLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.GONE)
            {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void hideLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.VISIBLE)
            {
                loading.setVisibility(View.GONE);
            }
        }
    }

}

