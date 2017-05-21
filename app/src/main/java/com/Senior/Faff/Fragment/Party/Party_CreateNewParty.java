package com.Senior.Faff.Fragment.Party;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.Promotion.PromotionRecyclerViewAdapter;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.CreatePartyManager;

import com.Senior.Faff.R;
import com.Senior.Faff.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Party_CreateNewParty extends AppCompatActivity {
    private com.google.android.gms.maps.model.Marker mCurrLocationMarker;
    private int MAP_REQUEST_CODE = 20;
    private LatLng myLocation;
    private GoogleApiClient googleApiClient;
    private Location location;
    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri

    public static int image_count = 0;                                    //number of images

    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private GoogleMap mMap;
    private EditText name,description,people,address,appointment,telephone;
    private ImageView party_pic;
    private Spinner spinner1;
    private String getlocation;
    private Button add_rule,create,rule;
    private Button add_pic, cancle_pic;
    private CreatePartyManager manager;
    private String roomid,userid;
    private Party party;
    private ArrayList<String> rule_gender;
    private String rule_age;
    private String createby;
    private String getrule;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party__create_new_party);
        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        people = (EditText)findViewById(R.id.people);
        address = (EditText)findViewById(R.id.address);
        appointment = (EditText)findViewById(R.id.period);
        telephone = (EditText)findViewById(R.id.telephone);
        add_rule = (Button)findViewById(R.id.rule);
        create = (Button)findViewById(R.id.next);
        add_pic = (Button) findViewById(R.id.picture);
        party_pic = (ImageView) findViewById(R.id.image_view);
        rule_gender = new ArrayList<>();
        Bundle args = getIntent().getExtras();
        if(args != null){
          userid  = args.getString(UserProfile.Column.UserID);
            createby = args.getString(UserProfile.Column.Name);

        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Party");
        long unixTime = System.currentTimeMillis() / 1000L;
        roomid = userid+ "ppap" + String.valueOf(unixTime);

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_count<1)
                {
                    Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //sdintent.setType("image/*");
                    startActivityForResult(sdintent, request_code);


                }
            }
        });

        add_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Party_CreateNewParty.this,Party_CreateNewParty_map.class);
                if(rule_gender != null) {
                    for (int i = 0; i < rule_gender.size(); i++) {
                        if (i == 0) {
                            getrule = rule_gender.get(i);
                        } else
                            getrule = getrule + rule_gender.get(i);
                        if (i != rule_gender.size() - 1) {
                            getrule = getrule + ",";
                        }
                    }
                }
                intent.putExtra(Party.Column.RoomID, roomid );
                intent.putExtra(Party.Column.Create_by_name, name.getText().toString());
                intent.putExtra(Party.Column.Description,description.getText().toString());
                intent.putExtra(Party.Column.People, Integer.parseInt(people.getText().toString()));
                intent.putExtra(Party.Column.Address,address.getText().toString());
                intent.putExtra(Party.Column.Appointment,appointment.getText().toString());
                intent.putExtra(Party.Column.Telephone,telephone.getText().toString());
                intent.putExtra(Party.Column.Rule_gender,getrule);
                intent.putExtra(Party.Column.Rule_age,rule_age);
                intent.putExtra(UserProfile.Column.UserID,userid);
                intent.putExtra(UserProfile.Column.Name,createby);
                String[] st = imgPath.get(0).split("/");
                String path = new Gson().toJson(imgPath);
                intent.putExtra(Party.Column.picture, path);
                startActivityForResult(intent, MAP_REQUEST_CODE);
            }
        });


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
                Bitmap b = BitmapFactory.decodeFile(imgPath.get(image_count));
                try {
                    b = new Helper().modifyOrientation(b, imgPath.get(image_count));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmap.add(b);
                //convert to byte
                party_pic.setVisibility(View.VISIBLE);
                Bitmap b_tmp = new Helper().getResizedBitmap(b, 400, 400);
                party_pic.setImageBitmap(b_tmp);

                cancle_pic = (Button) findViewById(R.id.cancle_image) ;
                cancle_pic.setVisibility(View.VISIBLE);
                cancle_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        party_pic.setVisibility(View.GONE);
                        image_count--;
                        bmap.clear();
                        imgPath.clear();
                        cancle_pic.setVisibility(View.GONE);
                    }
                });
//                imgByte.add(stream.toByteArray());
                image_count++;

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
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Party_CreateNewParty.this);
        View promptView = layoutInflater.inflate(R.layout.rule_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Party_CreateNewParty.this);
        alertDialogBuilder.setView(promptView);
        final  AlertDialog alert = alertDialogBuilder.create();
        // final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
      /*  alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Main2Activity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/

        Button btn_1 = (Button)promptView.findViewById(R.id.btn1);
        Button btn_2 = (Button) promptView.findViewById(R.id.btn2);
     final  CheckBox chk1 = (CheckBox)promptView.findViewById(R.id.chk1);
     final  CheckBox chk2 = (CheckBox)promptView.findViewById(R.id.chk2);
      final  EditText age = (EditText)promptView.findViewById(R.id.age);



        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.cancel();

            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chk1.isChecked()){
                    rule_gender.add("Female");
                }
                if(chk2.isChecked()){
                    rule_gender.add("Male");
                }
                if(!(chk1.isChecked() || chk2.isChecked()))
                {
                    rule_gender.add("Male");
                    rule_gender.add("Female");
                }
                if(age.getText().toString() != null) {
                    rule_age = age.getText().toString();
                }
                alert.cancel();
            }
        });
        // create an alert dialog

        alert.show();

    }

}
