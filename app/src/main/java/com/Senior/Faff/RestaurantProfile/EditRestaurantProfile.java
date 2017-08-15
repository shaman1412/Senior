package com.Senior.Faff.RestaurantProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.Senior.Faff.AddMap;
import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.ListType;
import com.Senior.Faff.Model.Party;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.Model.UserProfile;
import com.Senior.Faff.utils.Helper;
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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditRestaurantProfile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private final int MAP_REQUEST_CODE = 20;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private GoogleApiClient googleApiClient;
    private LatLng myLocation;
    private EditText name,description,period,address,telephone;
    private Toolbar toolbar;
    private String user_id;
    private String resid;
    private ArrayList<String> type_list;
    private Button next;
    private Spinner type;
    private Context mcontext;
    private ListType list_adapter;
    private RecyclerView mRecyclerView;
    private  Restaurant restaurant;
    private String type_check;
    private String getlocation;
    private boolean first = true;
    private GoogleMap mMap;
    private Location location;
    private String send_location;
    private ArrayList<String> favourite_type;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
    public static int image_count = 0;                                  //number of images
    private Button upload;
    private EditReataurantProfileRecyclerView adapter;
    private static String old_picture="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__restaurant_profile);
        setTitle("Edit Restaurant");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        favourite_type = new  ArrayList<String>();
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
              String k =  name.getText().toString();
                restaurant = new Restaurant(name.getText().toString(),address.getText().toString(),description.getText().toString(),period.getText().toString(),telephone.getText().toString(),user_id, type_check);
                restaurant.setresId(resid);
                restaurant.setLocation(getlocation);
                String img_list = new Gson().toJson(imgPath);
                restaurant.setPicture(img_list);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                restaurant.setCreate_time(timestamp);
                new EditRestProfile().execute(restaurant);

//                new updaterestaurant().execute(restaurant);
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
                if(favourite_type != null) {
                    for (int i = 0; i < favourite_type.size(); i++) {
                        if (text.equals(favourite_type.get(i))) {
                            same = true;
                        }
                    }
                }
                if(same == false && first == false) {
                    favourite_type.add(text);
                    list_adapter = new ListType(favourite_type, mcontext);
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

        upload = (Button) findViewById(R.id.upload_button);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //sdintent.setType("image/*");
                startActivityForResult(sdintent, request_code);
            }
        });
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
                Log.i("TEST:", "img_path from activity result : "+imgPath.get(image_count));
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new EditReataurantProfileRecyclerView(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
              }
              else if(requestCode == MAP_REQUEST_CODE) {
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
                  Toast.makeText(EditRestaurantProfile.this,getlocation,Toast.LENGTH_SHORT).show();
        }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(EditRestaurantProfile.this, AddMap.class);
                intent.putExtra(Party.Column.Location, getlocation);
                startActivityForResult(intent, MAP_REQUEST_CODE);
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
            mMap.setMyLocationEnabled(true);
   /*         LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));*/
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
                getlocation = lola;
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private class EditRestProfile extends AsyncTask<Restaurant, String, String> {

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
                EditRestaurantProfile.EditRestProfile.this.imgPath = new Gson().fromJson(img_path_tmp, ArrayList.class);

                String[] arr_tmp = old_picture.split("/");
                if(EditRestaurantProfile.EditRestProfile.this.imgPath.size()!=0)
                {
                    paras.put("old_filename", arr_tmp[arr_tmp.length-1]);
                }

                URL url = new URL("https://faff-1489402013619.appspot.com/res_profile/update/"+params[0].getresId());

                Helper hp = new Helper();
                hp.setRequest_method("PUT");
                result = hp.multipartRequest(url.toString(),paras, EditRestaurantProfile.EditRestProfile.this.imgPath, "image", "image/jpeg");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                Log.i("TEST:", "result : "+result);
                finish();
                Intent i = new Intent(mcontext, Main2Activity.class);
                i.putExtra(UserProfile.Column.UserID, user_id);
                startActivity(i);
                //Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(mcontext, "Fail on retrieve result", Toast.LENGTH_SHORT).show();
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
                if(respro.getPicture()!=null)
                {
                    old_picture = respro.getPicture();


                }
                if(respro.getTypefood() != null){
                    String[] list = respro.getTypefood().split(",");
                    favourite_type = new ArrayList<String>();
                    for(int i = 0; i < list.length; i++){
                        favourite_type.add(list[i]);
                    }
                    list_adapter = new ListType(favourite_type, mcontext);
                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
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
