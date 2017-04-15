package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Show_party_profile extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private LatLng myLocation;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private Context mcontext;
    private Toolbar toolbar;
    private TextView name,status,appointment,description,address,telephone;
    private ImageView create_image;
    private TextView createby;
    private Button send_request;
    private RecyclerView mRecyclerView;
    private String userid,partyid;
    private ArrayList<Party> party_list;
    private String key;
    private String Userid;
    private String olduserid;
    private Button sent_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_party_profile);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_party);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextView)findViewById(R.id.name);
        status=  (TextView)findViewById(R.id.status);
        appointment = (TextView)findViewById(R.id.appointment);
        description = (TextView)findViewById(R.id.description);
        createby = (TextView)findViewById(R.id.createby);
        create_image = (ImageView) findViewById(R.id.create_image);
        send_request = (Button)findViewById(R.id.send_request);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        address = (TextView)findViewById(R.id.address);
        telephone = (TextView)findViewById(R.id.telephone);
        sent_request = (Button)findViewById(R.id.sent_request);
        Userid = "peeranat";


        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    new getData().execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private class getData extends AsyncTask<String, String, Party > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;

        @Override
        protected Party doInBackground(String... args) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("All_Room");

            mRootRef.orderByChild("roomID")
                    .equalTo("a1412ppap").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot dataSnapshot){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        key = postSnapshot.getKey();
                        Party post= postSnapshot.getValue(Party.class);
                        setvalue(post);
                    }


                    //showlist(listview, party_list, resId,gender,age);
                    //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Cant connect database",Toast.LENGTH_SHORT).show();
                }

            });
            return null;
        }

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
                        Double.parseDouble(pos[0]));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        8));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(myLocation);
                markerOptions.title(res_name);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                //    getlocation = location.getLatitude() + ","  + location.getLongitude();

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
    public void  setvalue(Party partypro){
        name.setText(partypro.getName());
        description.setText(partypro.getDescription());
        appointment.setText(partypro.getAppointment());
        enableMyLocation(partypro.getLocation(),partypro.getName());
        address.setText(partypro.getAddress());
        createby.setText(partypro.getCreatename());
        telephone.setText(partypro.getTelephone());
        setTitle(partypro.getName());
        olduserid = partypro.getRequest();

        if(olduserid != null) {
            String[] a = olduserid.split(",");
            for(int i= 0; i< a.length ; i++) {
                if(a[i].equals(Userid)) {
                    View sd = findViewById(R.id.send_request);
                    sd.setVisibility(View.GONE);
                    View st = findViewById(R.id.sent_request);
                    st.setVisibility(View.VISIBLE);
                }
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(UserProfile.Column.UserID,olduserid);
        bundle.putString("key",key);
        party_member_accept fragment_party = new party_member_accept();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment_party.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.member,fragment_party).commit();
    }
    public void sendRequest(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");
        if(olduserid != null){
            Userid = olduserid + "," + Userid  ;
        }
        mDatabase.child(key).child("request").setValue(Userid);


    }
}
