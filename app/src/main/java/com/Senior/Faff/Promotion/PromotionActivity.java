package com.Senior.Faff.Promotion;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.AddMap;
import com.Senior.Faff.R;
import com.Senior.Faff.Model.Party;
import com.Senior.Faff.Model.Promotion;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.Model.RestaurantPromotion;
import com.Senior.Faff.Model.UserProfile;
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PromotionActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback  {

    public static final String TAG = PromotionActivity.class.getSimpleName();

    private Button addPromotion;
    private EditText Title;
    private Button uploadPicture;
    private EditText StartDate;
    private EditText EndDate;
    private EditText PromotionDetail;
    private EditText Location;
    private Context mContext;
    private RecyclerView mRecyclerView;
    //    private ListType list_adapter;
    private boolean first = true;
//    private String type_check;

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
//    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data

    public static int image_count = 0;                                    //number of images

    public static PromotionRecyclerViewAdapter adapter;
    private static String resid;
    private String userid;
//    private Spinner type;
//    private ArrayList<String> type_list;
    private final int MAP_REQUEST_CODE = 20;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private GoogleApiClient googleApiClient;
    private Location location;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private GoogleMap mMap;
    private String getlocation;
    private TextView showlocation;
    private String send_location;
    private static String promotionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_promotion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        resid = getIntent().getExtras().getString(Restaurant.Column.ResID);
        userid = getIntent().getExtras().getString(UserProfile.Column.UserID);
        mContext = this;
//        type_list = new ArrayList<>();

//        type = (Spinner) findViewById(R.id.type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        addPromotion = (Button) findViewById(R.id.addPromotion);
        Title = (EditText) findViewById(R.id.title);
        uploadPicture = (Button) findViewById(R.id.titlePicture);
        StartDate = (EditText) findViewById(R.id.startDate);
        EndDate = (EditText) findViewById(R.id.endDate);
        PromotionDetail = (EditText) findViewById(R.id.promotionDetail);

        //Need edition for more folder gallery to select
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //sdintent.setType("image/*");
                startActivityForResult(sdintent, request_code);

            }
        });

        addPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<String> list_get = list_adapter.getlist();
//                for (int i = 0; i < list_get.size(); i++) {
//                    if (i == 0) {
//                        type_check = list_get.get(i);
//                    } else {
//                        type_check = type_check + list_get.get(i);
//                    }
//                    if (i != list_get.size() - 1) {
//                        type_check = type_check + ",";
//                    }
//                }

                String title = Title.getText().toString();
                String startDate = StartDate.getText().toString();
                String endDate = EndDate.getText().toString();
                String promotionDetail = PromotionDetail.getText().toString();

                String img_path_tmp = new Gson().toJson(imgPath);
                if(getlocation != null) {
                    Promotion promotion = new Promotion(title, img_path_tmp, startDate, endDate, promotionDetail, getlocation);
                    promotionid = String.valueOf(System.currentTimeMillis())+"-"+resid;
                    promotion.setId(promotionid);
                    PromotionActivity.AddPromotion add_pro = new PromotionActivity.AddPromotion();
                    add_pro.execute(promotion);
                }else{
                    Toast.makeText(mContext,"Please complete blank",Toast.LENGTH_SHORT).show();
                }
                bmap.clear();
                imgPath.clear();
//                imgByte.clear();
                image_count = 0;
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
    protected void onDestroy() {
        image_count = 0;
        bmap = new ArrayList<>();
        imgPath = new ArrayList<>();
        super.onDestroy();
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
                Bitmap bm = BitmapFactory.decodeFile(imgPath.get(image_count));
                try {
                    bm = new Helper().modifyOrientation(bm, imgPath.get(image_count));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmap.add(bm);
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new PromotionRecyclerViewAdapter(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            }else if(requestCode == MAP_REQUEST_CODE) {
                getlocation = data.getStringExtra(Restaurant.Column.Location);
                send_location = getlocation;
                String[] split = getlocation.split(",");
                LatLng lola =  new LatLng(Double.parseDouble(split[0]),Double.parseDouble(split[1]));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lola,11));
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(lola);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
            }
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
    public void onLocationChanged(android.location.Location location) {

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
                Intent intent = new Intent(mContext, AddMap.class);
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

    private class AddPromotion extends AsyncTask<Promotion, String, String> {

        private ArrayList<String> imgPath = new ArrayList<>();
        String result = "";

        @Override
        protected String doInBackground(Promotion... params) {
            try {
                Map<String, String> paras = new HashMap<>();
                paras.put(Promotion.Column.ID, String.valueOf(params[0].getId()));
                paras.put(Promotion.Column.Title, params[0].getTitle());
//                paras.put(Promotion.Column.Type, params[0].getType());
                paras.put(Promotion.Column.StartDate, params[0].getStartDate());
                paras.put(Promotion.Column.EndDate, params[0].getEndDate());
                paras.put(Promotion.Column.PromotionDetail, params[0].getPromotionDetail());
                paras.put(Promotion.Column.Location, params[0].getGoogleMapLink());
                paras.put(Promotion.Column.Userid, userid);
                paras.put(Promotion.Column.ID, params[0].getId());

                String img_path_tmp = params[0].getPromotionpictureurl();
                AddPromotion.this.imgPath = new Gson().fromJson(img_path_tmp, ArrayList.class);

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/new_promotion");

                Helper hp = new Helper();
                hp.setRequest_method("POST");
                result = hp.multipartRequest(url.toString(), paras, AddPromotion.this.imgPath, "image", "image/jpeg");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {

                PromotionActivity.Linking lk = new PromotionActivity.Linking();
                lk.execute(new RestaurantPromotion(resid, promotionid));

//                getCount gc = new getCount();
//                gc.execute("");

                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Linking extends AsyncTask<RestaurantPromotion, String, String> {
        private HttpURLConnection connection;
        private int responseCode;

        @Override
        protected String doInBackground(RestaurantPromotion... params) {
            try {
                JSONObject paras = new JSONObject();
                paras.put(RestaurantPromotion.Column.resid, params[0].getResid());
                paras.put(RestaurantPromotion.Column.promotionid, params[0].getPromotionid());

                URL url = new URL("https://faff-1489402013619.appspot.com/restaurant_promotion/create");
                //URL url = new URL("http://localhost:8080/promotion_list/new_promotion");

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                bufferedWriter.write(getPostDataString(paras));
                bufferedWriter.flush();
                bufferedWriter.close();
                out.close();

                responseCode = connection.getResponseCode();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (responseCode == 200) {
                Log.i("TEST:", " res_promo This is success response status from server: " + responseCode);

                return String.valueOf(responseCode);
            } else {
                Log.i("Request Status", "Tsis is failure response status from server: " + responseCode);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                finish();
                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
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

//    private class getCount extends AsyncTask<String, String, String> {
//
//        private String result;
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/get_count");
//
//                Helper hp = new Helper();
//                hp.setRequest_method("GET");
//                result = hp.getRequest(url.toString());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result != "") {
//                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
//                try {
//                    JSONArray item = new JSONArray(result);
//                    String n = item.getJSONObject(0).getString("n");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//    }
//

}