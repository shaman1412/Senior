package com.Senior.Faff.Promotion;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Add_map;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Edit_RestaurantProfile;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.Restaurant;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Edit_promotion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private LatLng myLocation;
    private Context mContext;
    private Toolbar toolbar;
    private Button uploadPicture;
    private static final int request_code = 1;                          //request code for OnClick result
    private final int MAP_REQUEST_CODE = 20;
    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
//    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    public static int image_count = 0;                                    //number of images
    private Edit_promotion_RecyclerView adapter;
    private String getlocation;
    private Location location;
    private String send_location;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promotion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = this;
        Intent i = getIntent();
        String id = i.getExtras().getString("id");

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                //Log.i(TAG, "  item is : "+item.toString());

                Promotion data = new Gson().fromJson(item.toString(), Promotion.class);
                String[] arr_url = item.getString("promotionpictureurl").split(",");

                EditText textView1 = (EditText) findViewById(R.id.title);
                textView1.setText(data.getTitle());

                EditText textView2 = (EditText) findViewById(R.id.startDate);
                textView2.setText(data.getStartDate());

                EditText textView3 = (EditText) findViewById(R.id.endDate);
                textView3.setText(data.getEndDate());

                EditText textView4 = (EditText) findViewById(R.id.promotionDetail);
                textView4.setText(data.getPromotionDetail());


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

                uploadPicture = (Button) findViewById(R.id.upload_button);

                uploadPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //sdintent.setType("image/*");
                        startActivityForResult(sdintent, request_code);

                    }
                });

                enableMyLocation(data.getGoogleMapLink(),data.getTitle());

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlist1);
                PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(mContext, arr_url);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);

            }
        });
        lsp.execute(id);
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

                adapter = new Edit_promotion_RecyclerView(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            } else if (requestCode == MAP_REQUEST_CODE) {
                getlocation = data.getStringExtra(Restaurant.Column.Location);
                send_location = getlocation;
                String[] split = getlocation.split(",");
                LatLng lola = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lola, 11));
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(lola);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                Toast.makeText(mContext, getlocation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private static class ListPromotion extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public Edit_promotion.ListPromotion.AsyncResponse delegate = null;

        public ListPromotion(Edit_promotion.ListPromotion.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/" + Integer.parseInt(params[0]));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(mContext, Add_map.class);
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
}
