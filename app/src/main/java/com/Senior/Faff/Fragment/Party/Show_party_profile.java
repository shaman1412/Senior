package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.Party;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Show_party_profile extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private LatLng myLocation;
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private static Context mcontext;
    private Toolbar toolbar;
    private TextView name,status,appointment,description,address,telephone;
    private ImageView create_image;
    private ImageView party_image;
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
    private String partid;
    private LinearLayout showcreate;
    private int number_party = 0;
    private int count = 0;
    private TextView cmember,maxmember;
    private  Button enter_chat,leave_group;
    private String send_location;

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
        mcontext= this;
        enter_chat = (Button)findViewById(R.id.enter_room);
        leave_group = (Button)findViewById(R.id.Leave_group);


        name = (TextView)findViewById(R.id.name);
        status=  (TextView)findViewById(R.id.status);
        appointment = (TextView)findViewById(R.id.appointment);
        description = (TextView)findViewById(R.id.description);
        createby = (TextView)findViewById(R.id.createby);
        create_image = (ImageView) findViewById(R.id.create_image);
        party_image = (ImageView) findViewById(R.id.profile_image);
        send_request = (Button)findViewById(R.id.send_request);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        address = (TextView)findViewById(R.id.address);
        telephone = (TextView)findViewById(R.id.telephone);
        sent_request = (Button)findViewById(R.id.sent_request);
        showcreate = (LinearLayout)findViewById(R.id.showcreate);
        cmember = (TextView)findViewById(R.id.people);
        maxmember = (TextView)findViewById(R.id.max);

        bundle = new Bundle();
        Bundle args = getIntent().getExtras();
        start = true;
        if(args != null) {
            own_userid = args.getString(UserProfile.Column.UserID, null);
            partyid = args.getString(Party.Column.RoomID,null);
            Userid =  own_userid;
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (send_location == null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(Show_party_profile.this, MapsActivity.class);
                    intent.putExtra(Party.Column.Location, send_location);
                    startActivity(intent);
                }
            });
        }
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
                    .equalTo(partyid).addValueEventListener(new ValueEventListener() {
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
                        Double.parseDouble(pos[1]));
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
    public void  setvalue(final Party partypro) {
        name.setText(partypro.getName());
        description.setText("       "+partypro.getDescription());
        appointment.setText("       "+partypro.getAppointment());
        send_location  = partypro.getLocation();
        enableMyLocation(partypro.getLocation(), partypro.getName());
        address.setText("       "+partypro.getAddress());
        createby.setText(partypro.getCreatename());
        telephone.setText("       "+partypro.getTelephone());
        setTitle(partypro.getName());
        olduserid_request = partypro.getRequest();
        olduserid_accept = partypro.getAccept();
        number_party = partypro.getPeople();
        maxmember.setText(Integer.toString(number_party));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(partypro.getName());

        if(own_userid.equals(createby)) {
            View a = findViewById(R.id.request);
            a.setVisibility(View.VISIBLE);
        }
        if(olduserid_accept != null){
            String[] countsting =   olduserid_accept.split(",");
            count = countsting.length;
            cmember.setText(Integer.toString(count));
        }else{
            cmember.setText("0");
        }

        String pic_url = partypro.getPicture();
        Log.i("TEST: ", pic_url);
        if(pic_url!=null)
        {
            String[] tmp = pic_url.split("/");
            StorageReference load = FirebaseStorage.getInstance().getReference().child(tmp[1]).child(tmp[2]);
            load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mcontext).load(uri.toString()).resize(500,500).into(party_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    exception.printStackTrace();
                }
            });
        }

        ShowParty sh = new ShowParty(new ShowParty.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                ArrayList<String[]> bitmap_url_list = new ArrayList<>();

                String[] arr_url = item.getString("picture").split(",");

                if(arr_url!=null)
                {
                    Picasso.with(mcontext).load(arr_url[0]).resize(500, 500).into(create_image);
                }


            }
        });
        sh.execute(own_userid);


        status.setText("None");
        status.setTextColor(Color.GRAY);
        if (olduserid_request != null ) {
            String[] a = olduserid_request.split(",");
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals(Userid)) {
                    View sd = findViewById(R.id.send_request);
                    sd.setVisibility(View.GONE);
                    View st = findViewById(R.id.sent_request);
                    st.setVisibility(View.VISIBLE);
                    status.setText("Request");
                    status.setTextColor(Color.RED);

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
                    st.setVisibility(View.GONE);
                    View ec = findViewById(R.id.endandchat);
                    ec.setVisibility(View.VISIBLE);
                    status.setText("Joined");
                    status.setTextColor(Color.GREEN);
                    chatandleave();
                }
            }
        }
        showcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mcontext, ShowUserprofile.class);
                intent.putExtra(UserProfile.Column.UserID, partypro.getCreateid());
                intent.putExtra(UserProfile.Column.Ownerid,own_userid);
                startActivity(intent);
            }
        });
        if(partypro.getCreateid().equals(own_userid)) {
            bundle.putBoolean("host", true);
        }else{
            bundle.putBoolean("host", false);
        }
            bundle.putString(UserProfile.Column.UserID_accept, olduserid_accept);
            bundle.putString(UserProfile.Column.UserID_request, olduserid_request);
            bundle.putString("key", key);

            if (start) {
                if(partypro.getCreateid().equals(own_userid)) {
                    party_member_request fragment_party = new party_member_request();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragtran = fragmentManager.beginTransaction();
                    fragment_party.setArguments(bundle);
                    fragtran.add(R.id.request, fragment_party).commit();
                }else{

                    View ec = findViewById(R.id.card_request);
                    ec.setVisibility(View.GONE);
                }

                    party_member_accept fragment_accept = new party_member_accept();
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction fragtran1 = fragmentManager1.beginTransaction();
                    fragment_accept.setArguments(bundle);
                    fragtran1.add(R.id.member, fragment_accept).commit();


                start = false;
            }


        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        }

    private static class ShowParty extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public Show_party_profile.ShowParty.AsyncResponse delegate = null;

        public ShowParty(Show_party_profile.ShowParty.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://faff-1489402013619.appspot.com/user/" + params[0].toString());
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
                Toast.makeText(mcontext, "Fail to retrieve from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendRequest(){

        if( count < number_party) {
            View sd = findViewById(R.id.send_request);
            sd.setVisibility(View.GONE);
            View st = findViewById(R.id.sent_request);
            st.setVisibility(View.VISIBLE);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");
            if (olduserid_request != null) {
                Userid = olduserid_request + "," + Userid;
            }
            mDatabase.child(key).child("request").setValue(Userid);

        }
        Toast.makeText(mcontext,"Member is full",Toast.LENGTH_SHORT);
    }
    public void chatandleave(){
        enter_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");
                if (olduserid_accept != null ) {
                    boolean first = true;
                    boolean change = false;
                    String[] b = olduserid_accept.split(",");
                    for (int j = 0; j < b.length; j++) {
                        if (!(b[j].equals(own_userid))) {
                            if(first) {
                                Userid = b[j];
                                first = false;
                                change = true;
                            }
                            else {
                                Userid = "," + Userid + b[j];
                                change = true;
                            }
                        }
                    }
                    if(!change)
                    {
                        Userid = null;
                    }
                    mDatabase.child(key).child("accept").setValue(Userid);
                }

            }
        });

    }
}
