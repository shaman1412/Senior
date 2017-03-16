package com.Senior.Faff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.Senior.Faff.Fragment.Adapter.Home_adpater;
import com.Senior.Faff.Fragment.Adapter.Party_adapter;
import com.Senior.Faff.Fragment.MainMenu.MainHome_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainNearby_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainParty_fragment;
import com.Senior.Faff.Promotion.PromotionActivity;
import com.Senior.Faff.RestaurantProfile.Option_RestaurantFragment;
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.UserProfile.ProfileManager;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.chat.ChatMainActivity;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
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

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       firebase a  = new firebase();
      // a.writeNewUser();
        //a.change();
/*        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
       // File files = new File("C:/Users/Acer/Desktop/new nsc/bubble-512");
        Uri file = Uri.fromFile(new File("@drawable/addphoto.png"));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Main2Activity.this,"error",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
               // Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });*/

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                long count = dataSnapshot.child("All_Room").getChildrenCount();
                ArrayList<Party> m = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.child("All_Room").getChildren()) {
                Party post= postSnapshot.getValue(Party.class);
                   m.add(post);
                }

                Toast.makeText(Main2Activity.this,"hi",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this,"Cant connect database",Toast.LENGTH_SHORT).show();
            }
        });
 /*       mRootRef.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Party post = dataSnapshot.getValue(Party.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                 long count = dataSnapshot.getChildrenCount();
                Party posts = dataSnapshot.child("messages").getValue(Party.class);
                String commentKey = dataSnapshot.getKey();
                String key = dataSnapshot.child("All_Room").getKey();
                Toast.makeText(Main2Activity.this,"hi",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this,"Cant connect database",Toast.LENGTH_SHORT).show();
            }
        });*/

        setContentView(R.layout.activity_main2);
        setTitle("");

        //get map key-value of this activity in args
        //pager = (ViewPager)findViewById(R.id.pager);
        context = this;

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);


        pager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        //tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        Bundle args;
        if((args = getIntent().getExtras()) != null) {
            args = getIntent().getExtras();
            //int id = profileManager.getID(args.getString(UserAuthen.Column.USERNAME));
             String  id = args.getString("userid");
            userProfile.setId(id);
        }

        MainHome_Fragment fragment_home = new MainHome_Fragment();
        FragmentManager fragmentManager_home = getSupportFragmentManager();
        fragmentManager_home.beginTransaction().replace(R.id.flContent,fragment_home).commit();
      /*tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);*/
        //tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
/*            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        //tabLayout.setupWithViewPager(pager);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.AddPromotion:
                                ChangeTabToAddPromotion();
                                break;
                            case R.id.Party:
                                MainParty_fragment fragment_party = new MainParty_fragment();
                                FragmentManager fragmentManager_party = getSupportFragmentManager();
                                fragmentManager_party.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent,fragment_party)
                                        .commit();

                                break;
                            case R.id.Home:
                                MainHome_Fragment fragment_home = new MainHome_Fragment();
                                FragmentManager fragmentManager_home = getSupportFragmentManager();
                                fragmentManager_home.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent,fragment_home)
                                        .commit();
                                break;
                            case R.id.Nearby:
                                MainNearby_Fragment fragment_nearby = new MainNearby_Fragment();
                                FragmentManager fragmentManager_nearby = getSupportFragmentManager();
                                fragmentManager_nearby.beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                        .replace(R.id.flContent,fragment_nearby)
                                        .commit();
                                break;
                        }

                        return true;
                    }
                });

        //ActionBarDrawerToggle drawerToggle

    }

    private void ChangeTabToAddPromotion() {
        Intent intent = new Intent(context, PromotionActivity.class);
        startActivity(intent);
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
        if(id == R.id.dialog_filter){
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
        String id = userProfile.getId();
        String User_id = String.valueOf(id);        // ??? for what ??? ไม่บอก 5555

        switch(menuItem.getItemId()) {
            case R.id.Home:
                Intent Home_intent = new Intent(context, InsertUserProfile.class);
                startActivity(Home_intent);
                break;
            case R.id.NotificationUser:

                break;
            case R.id.UserProfile:
                String a = id;
                Intent intent = new Intent(context, ShowUserprofile.class);
                intent.putExtra(UserProfile.Column.ID,User_id);
                startActivity(intent);
                break;
            case R.id.Favourite:
                Intent b = new Intent(context,ChatMainActivity.class);
                startActivity(b);
                break;
            case R.id.History:

                break;
            case R.id.RestaurantProfile:
                Bundle bundle = new Bundle();
                bundle.putString("userid",User_id);
                //set Fragmentclass Arguments
                Option_RestaurantFragment option = new Option_RestaurantFragment();
                option.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flContent,option).commit();
                break;
            case R.id.AddPromotion:
                Intent i = new Intent(context, PromotionActivity.class);
                startActivity(i);
                break;
            case R.id.NotificationRe:

                break;
            case R.id.Setting:

                break;
            case R.id.Contact:

                break;
            case R.id.Logout:

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
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
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
        TextView btn_1 = (TextView)promptView.findViewById(R.id.btn1);
        TextView btn_2 = (TextView) promptView.findViewById(R.id.btn2);
        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.cancel();

            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

            }
        });
        // create an alert dialog

        alert.show();

    }
    public  void read(){
            }

}
