package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
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

import java.net.HttpURLConnection;
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
    private String olduserid_request,olduserid_accept;
    private Button sent_request;
    private String own_userid;
    private Bundle save;
    private Bundle bundle;
    private boolean start;

    @Override
    protected void onResume() {
        super.onResume();
        new getDataMember().execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // save = savedInstanceState;
        setContentView(R.layout.activity_show_party_profile);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_party);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mcontext= this;
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
        bundle = new Bundle();
        Bundle args = getIntent().getExtras();
        start = true;
        if(args != null) {
            own_userid = args.getString(UserProfile.Column.UserID, null);
            Userid =  own_userid;
        }


        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private class getDataMember extends AsyncTask<String, String, Party > {

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
    private class getDataRequest extends AsyncTask<String, String, Party > {

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
    public void  setvalue(Party partypro) {
        name.setText(partypro.getName());
        description.setText(partypro.getDescription());
        appointment.setText(partypro.getAppointment());
        enableMyLocation(partypro.getLocation(), partypro.getName());
        address.setText(partypro.getAddress());
        createby.setText(partypro.getCreatename());
        telephone.setText(partypro.getTelephone());
        setTitle(partypro.getName());
        olduserid_request = partypro.getRequest();
        olduserid_accept = partypro.getAccept();

        if (olduserid_request != null ) {
            String[] a = olduserid_request.split(",");
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals(Userid)) {
                    View sd = findViewById(R.id.send_request);
                    sd.setVisibility(View.GONE);
                    View st = findViewById(R.id.sent_request);
                    st.setVisibility(View.VISIBLE);

                }
            }
        }
        if (olduserid_accept != null ) {
            String[] b = olduserid_accept.split(",");
            for (int j = 0; j < b.length; j++) {
                if (b[j].equals(Userid)) {
                    View sd = findViewById(R.id.send_request);
                    sd.setVisibility(View.GONE);
                    View st = findViewById(R.id.sent_request);
                    st.setVisibility(View.VISIBLE);
                }
            }
        }


            bundle.putString(UserProfile.Column.UserID_accept, olduserid_accept);
            bundle.putString(UserProfile.Column.UserID_request, olduserid_request);
            bundle.putString("key", key);

            if (start) {
                party_member_request fragment_party = new party_member_request();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragtran = fragmentManager.beginTransaction();
                fragment_party.setArguments(bundle);
                fragtran.add(R.id.request, fragment_party).commit();

                party_member_accept fragment_accept = new party_member_accept();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragtran1 = fragmentManager1.beginTransaction();
                fragment_accept.setArguments(bundle);
                fragtran1.add(R.id.member, fragment_accept).commit();
                start = false;
            }

/*
        if( findViewById(R.id.member) != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment_party);
            ft.commit();
        }*/


        }

    public void sendRequest(){
        View sd = findViewById(R.id.send_request);
        sd.setVisibility(View.GONE);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");
        if(olduserid_request != null){
            Userid = olduserid_request + "," + Userid  ;
        }
        mDatabase.child(key).child("request").setValue(Userid);

    /*    Intent intent = new Intent(mcontext, Main2Activity.class);
        intent.putExtra(UserProfile.Column.UserID,own_userid);
        startActivity(intent);
*/
/*
        party_member_request fragment_party = new party_member_request();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragtran = fragmentManager.beginTransaction();
        fragment_party.setArguments(bundle);
        fragtran.add(R.id.member,fragment_party).commit();
*/

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
}
