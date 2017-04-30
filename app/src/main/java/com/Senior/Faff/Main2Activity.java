package com.Senior.Faff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.Senior.Faff.Fragment.Adapter.Home_adpater;
import com.Senior.Faff.Fragment.Adapter.Party_adapter;
import com.Senior.Faff.Fragment.MainMenu.MainHome_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainNearby_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainParty_fragment;
import com.Senior.Faff.Promotion.PromotionActivity;
import com.Senior.Faff.Promotion.PromotionShow;
import com.Senior.Faff.RestaurantProfile.Option_RestaurantFragment;
import com.Senior.Faff.UserProfile.Change_password;
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.UserProfile.ProfileManager;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.UserProfile.favorite_restaurant;
import com.Senior.Faff.chat.ChatMainActivity;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.model.filter_nearby_restaurant;
import com.Senior.Faff.utils.firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {

    private static final String Tag = Main2Activity.class.getSimpleName();

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
    private ProfileManager profileManager;
    private UserProfile userProfile;
    private Bundle bundle;
    private String userid;
    private String id, search_name;
    private EditText serach_box;
    private ArrayList<String> type_food;
    private float distance = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase a = new firebase();

        setContentView(R.layout.activity_main2);
        setTitle("");
        SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //get map key-value of this activity in args
        //pager = (ViewPager)findViewById(R.id.pager);
        context = this;


        // serach_box = (EditText)findViewById(R.id.serach_box);
/*        serach_box.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            search_name    =  serach_box.getText().toString();
                            search_name();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });*/

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


        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        Bundle args;
        if ((args = getIntent().getExtras()) != null) {
            args = getIntent().getExtras();
            id = args.getString(UserProfile.Column.UserID);
            editor.putString(UserProfile.Column.UserID, id);
            editor.commit();
            bundle = new Bundle();
            new getData().execute(id);

        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.Party:
                                MainParty_fragment fragment_party = new MainParty_fragment();
                                fragment_party.setArguments(bundle);
                                FragmentManager fragmentManager_party = getSupportFragmentManager();
                                fragmentManager_party.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent, fragment_party)
                                        .commit();

                                break;
                            case R.id.Home:
                                Bundle b = new Bundle();

                                MainHome_Fragment fragment_home = new MainHome_Fragment();
                                fragment_home.setArguments(bundle);
                                FragmentManager fragmentManager_home = getSupportFragmentManager();
                                fragmentManager_home.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent, fragment_home)
                                        .commit();
                                break;
                            case R.id.Nearby:
                                MainNearby_Fragment fragment_nearby = new MainNearby_Fragment();
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
        // Create a new fragment and specify the fragment to show based on nav item clicked

        //String User_id = String.valueOf(id);        // ??? for what ??? ไม่บอก 5555

        switch (menuItem.getItemId()) {
            case R.id.Home:
                Intent Home_intent = new Intent(context, InsertUserProfile.class);
                startActivity(Home_intent);
                break;
            case R.id.NotificationUser:
                Intent Home_intenta = new Intent(context, test_callservice.class);
                startActivity(Home_intenta);
                break;
            case R.id.UserProfile:
                Intent intent = new Intent(context, ShowUserprofile.class);
                intent.putExtra(UserProfile.Column.UserID, userid);
                intent.putExtra(UserProfile.Column.Ownerid,userid);
                startActivity(intent);
                break;
            case R.id.Favourite:
                Intent b = new Intent(context, favorite_restaurant.class);
                b.putExtra(UserProfile.Column.UserID, userid);
                startActivity(b);
                break;
            case R.id.ShowPromotion:
               Intent i = new Intent(context, ChatMainActivity.class);
                startActivity(i);
                break;
            case R.id.RestaurantProfile:
                Bundle bundle = new Bundle();
                bundle.putString("userid", userid);
                //set Fragmentclass Arguments

                Log.i(Tag, "  in Main2 userid is : " + userid);
                Option_RestaurantFragment option = new Option_RestaurantFragment();
                option.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flContent, option).commit();
                break;
            case R.id.change_pass:
                Intent ii = new Intent(context, Change_password.class);
                ii.putExtra(UserProfile.Column.UserID,id);
               startActivity(ii);
                break;

            case R.id.Logout:
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
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
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
                filter_nearby_restaurant filter = new filter_nearby_restaurant();
                if(type_food != null){
                    filter.setType(type_food);
                }
                if(distance != 0){
                    filter.setDistnace(distance);
                }
                MainNearby_Fragment fragment_nearby = new MainNearby_Fragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Restaurant.Column.TypeFood,filter);
                bundle.putInt("Postition",1);
/*                bundle.putStringArrayList(Restaurant.Column.TypeFood,type_food);
                bundle.putFloat("distance", distance);*/
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
                MainHome_Fragment fragment_home = new MainHome_Fragment();
                fragment_home.setArguments(bundle);
                FragmentManager fragmentManager_home = getSupportFragmentManager();
                fragmentManager_home.beginTransaction().replace(R.id.flContent, fragment_home).commit();
            } else {
                /*String message = getString(R.string.login_error_message);
                Toast.makeText(Main2Activity.this, message, Toast.LENGTH_SHORT).show();*/
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
