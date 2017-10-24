package com.Senior.Faff;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.Senior.Faff.Fragment.MainMenu.MainHomeFragment;
import com.Senior.Faff.Fragment.MainMenu.MainNearbyFragment;
import com.Senior.Faff.Fragment.MainMenu.MainPartyFragment;
import com.Senior.Faff.RestaurantProfile.OptionRestaurantFragment;
import com.Senior.Faff.UserProfile.ChangePassword;
import com.Senior.Faff.UserProfile.InsertUserProfile;
//import com.Senior.Faff.utils.ProfileManager;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.UserProfile.FavoriteRestaurant;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.Model.UserProfile;
import com.Senior.Faff.Model.FilterNearbyRestaurant;
import com.Senior.Faff.utils.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private static final String Tag = Main2Activity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ViewPager pager;
    private Context context;
    final int[] ICONS = new int[]{
            R.drawable.find_res,
            R.drawable.find_res,
            R.drawable.find_res,
            R.drawable.ic_launcher
    };
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TabLayout tabLayout;
//    private ProfileManager profileManager;
    private UserProfile userProfile;
    private Bundle bundle;
    private String userid;
    private String id, search_name;
    private EditText serach_box;
    private ArrayList<String> type_food;
    private float distance = 10;
    Bundle saveStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveStage = savedInstanceState;
        Firebase a = new Firebase();

        setContentView(R.layout.activity_main2);
        setTitle("");
        SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //get map key-value of this activity in args
        //pager = (ViewPager)findViewById(R.id.pager);
        context = this;

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("TEST:", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("TEST:s", "onAuthStateChanged:signed_out");
                }
            }
        };

        mAuth.signInAnonymously().addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("TEST:", "signInAnonymously:onComplete:" + task.isSuccessful());
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.i("TEST:", "signInAnonymously", task.getException());
//                    Toast.makeText(context, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);


        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        //tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


//        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        Bundle args;
        if ((args = getIntent().getExtras()) != null) {
            args = getIntent().getExtras();
            id = args.getString(UserProfile.Column.UserID);
            editor.putString(UserProfile.Column.UserID, id);
            editor.commit();
            bundle = new Bundle();
            new getData().execute(id);

        }else{
            id = sp.getString(UserProfile.Column.UserID, "nothing");
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.Party:
                                MainPartyFragment fragment_party = new MainPartyFragment();
                                fragment_party.setArguments(bundle);
                                FragmentManager fragmentManager_party = getSupportFragmentManager();
                                fragmentManager_party.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent, fragment_party)
                                        .commit();

                                break;
                            case R.id.Home:
                                Bundle b = new Bundle();

                                MainHomeFragment fragment_home = new MainHomeFragment();
                                fragment_home.setArguments(bundle);
                                FragmentManager fragmentManager_home = getSupportFragmentManager();
                                fragmentManager_home.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent, fragment_home)
                                        .commit();
                                break;
                            case R.id.Nearby:
                                MainNearbyFragment fragment_nearby = new MainNearbyFragment();
                                FragmentManager fragmentManager_nearby = getSupportFragmentManager();
                                fragmentManager_nearby.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent, fragment_nearby)
                                        .commit();
                                break;
                        }

                        return true;
                    }
                });

        //ActionBarDrawerToggle drawerToggle

    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.dialog_filter) {
            showInputDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.UserProfile:
                Intent intent = new Intent(context, ShowUserprofile.class);
                intent.putExtra(UserProfile.Column.UserID, userid);
                intent.putExtra(UserProfile.Column.Ownerid,userid);
                startActivity(intent);
                break;
            case R.id.Favourite:
                Intent b = new Intent(context, FavoriteRestaurant.class);
                b.putExtra(UserProfile.Column.UserID, userid);
                startActivity(b);
                break;
            case R.id.RestaurantProfile:
                Bundle bundle = new Bundle();
                bundle.putString("userid", userid);
                //set Fragmentclass Arguments

                Log.i(Tag, "  in Main2 userid is : " + userid);
                OptionRestaurantFragment option = new OptionRestaurantFragment();
                option.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flContent, option).commit();
                break;
            case R.id.change_pass:
                Intent ii = new Intent(context, ChangePassword.class);
                ii.putExtra(UserProfile.Column.UserID,id);
               startActivity(ii);
                break;

            case R.id.Logout:
                if (mAuthListener != null) {
                    mAuth.removeAuthStateListener(mAuthListener);
                }
                SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(UserProfile.Column.UserID, "nothing");
                editor.commit();
                Intent logout = new Intent(context, LoginActivity.class);
                startActivity(logout);
                finish();
                break;

            default:

        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Main2Activity.this);
        View promptView = layoutInflater.inflate(R.layout.main_dialog_filter, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
        alertDialogBuilder.setView(promptView);
        final AlertDialog alert = alertDialogBuilder.create();
        type_food = new ArrayList<>();

      final TextView getseek = (TextView)promptView.findViewById(R.id.getseek);
         SeekBar seek = (SeekBar)promptView.findViewById(R.id.seek);
        seek.setProgress(Math.round(distance));
        getseek.setText(String.valueOf(distance) + " Km");
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float progresValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresValue = (float)progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                distance = progresValue;
                getseek.setText(String.valueOf(distance) + " Km");
            }
        });
        TextView btn_1 = (TextView) promptView.findViewById(R.id.btn1);
        TextView btn_2 = (TextView) promptView.findViewById(R.id.btn2);
        //type_food = new ArrayList<>();



        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.cancel();

            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                FilterNearbyRestaurant filter = new FilterNearbyRestaurant();
                if(type_food != null){
                    filter.setType(type_food);
                }
                if(distance != 0){
                    filter.setDistnace(distance);
                }
                MainNearbyFragment fragment_nearby = new MainNearbyFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Restaurant.Column.TypeFood,filter);
                bundle.putInt("Postition",1);

                FragmentManager fragmentManager_nearby = getSupportFragmentManager();
                fragment_nearby.setArguments(bundle);
                fragmentManager_nearby.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.flContent, fragment_nearby)
                        .commit();
                alert.cancel();
            }
        });
        // create an alert dialog

        alert.show();

    }

    public void onCheckbox(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        CheckBox check_type = (CheckBox) v.findViewById(v.getId());

        if (checked) {
            type_food.add(check_type.getText().toString());
        } else {
            for (int i = 0; i < type_food.size(); i++) {
                if (type_food.get(i) == check_type.getText().toString()) {
                    type_food.remove(i);
                }
            }
        }
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
                UserProfile userPro = gson.fromJson(result.toString(), UserProfile.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }

        }

        @Override
        protected void onPostExecute(UserProfile userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {
                userProfile = userpro;
                userid = userProfile.getUserid();
                bundle.putString(UserProfile.Column.UserID, userProfile.getUserid());
                bundle.putString(UserProfile.Column.Name, userProfile.getName());
                bundle.putInt(UserProfile.Column.Age, userProfile.getAge());
                bundle.putInt(UserProfile.Column.Gender, userProfile.getGender());
                MainHomeFragment fragment_home = new MainHomeFragment();
                fragment_home.setArguments(bundle);
                FragmentManager fragmentManager_home = getSupportFragmentManager();
                try {
                    fragmentManager_home.beginTransaction().replace(R.id.flContent, fragment_home).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mAuthListener != null) {
                        mAuth.removeAuthStateListener(mAuthListener);
                    }
                    SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(UserProfile.Column.UserID, "nothing");
                    editor.commit();
                    Intent logout = new Intent(context, LoginActivity.class);
                    startActivity(logout);
                    finish();
                }
            } else {
                
                Intent intent = new Intent(context, InsertUserProfile.class);
                intent.putExtra(UserProfile.Column.UserID, id);
                startActivity(intent);
            }

        }

    }

    public void search_name() {
        Toast.makeText(context, "searched", Toast.LENGTH_SHORT);

    }


}
