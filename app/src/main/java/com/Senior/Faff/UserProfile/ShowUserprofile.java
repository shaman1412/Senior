package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.BitmapImageManager;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowUserprofile extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView text;
    private ProfileManager profileManager;
    private UserProfile userProfile;
    private String userid;
    private Context mcontext;
    private TextView name,age,address,email,telephone,gender;
    private ArrayList<String> favourite_type;
    private RecyclerView mRecyclerView;
    private List_typeNodel list_adapter;
    private FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_userprofile);
        setTitle("Profile");
        mcontext = this;
        Bundle args = getIntent().getExtras();
        userid = args.getString(UserProfile.Column.UserID,null);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        address = (TextView)findViewById(R.id.address);
        email = (TextView)findViewById(R.id.gemail);
        telephone = (TextView)findViewById(R.id.gtelephone);
        gender = (TextView)findViewById(R.id.ggender);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        fab = (FloatingActionButton)findViewById(R.id.fab);
/*        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("Title");*/


        text = (TextView) findViewById(R.id.text);
        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        //  String json = profileManager.getUserProfile(id);
        // text.setText(json);
        BitmapImageManager bitmap = new BitmapImageManager();
        ImageView image = (ImageView) findViewById(R.id.profile_image);
        image.setImageBitmap(bitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.woman, 100, 100));

        final NestedScrollView childScroll = (NestedScrollView) findViewById(R.id.scroll);
        if(userid != null){
           new getData().execute(userid);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(mcontext,UpdateUserProfile.class);
                intent.putExtra(UserProfile.Column.UserID,userid);
                startActivity(intent);
            }
        });

    }
    private class getData extends AsyncTask<String, String, UserProfile> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected UserProfile doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/user/" + args[0];
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
                UserProfile userPro  =  gson.fromJson(result.toString(),  UserProfile.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(UserProfile userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {
                name.setText(userpro.getName());
                age.setText(String.valueOf(userpro.getAge()));
                address.setText(userpro.getAddress());
                email.setText(userpro.getEmail());
                telephone.setText(userpro.getTelephone());
                switch (userpro.getGender()){
                    case 0:
                        gender.setText("Female");
                        break;
                    case 1:
                        gender.setText("Male");
                        break;
                }
                if(userpro.getFavourite_type() != null){
                    String[] list = userpro.getFavourite_type().split(",");
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
