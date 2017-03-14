package com.Senior.Faff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.Senior.Faff.Fragment.Adapter.Home_adpater;
import com.Senior.Faff.Fragment.Adapter.Party_adapter;
import com.Senior.Faff.Fragment.MainMenu.MainHome_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainNearby_Fragment;
import com.Senior.Faff.Fragment.MainMenu.MainParty_fragment;
import com.Senior.Faff.RestaurantProfile.Option_RestaurantFragment;
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.UserProfile.ProfileManager;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.firebase;

public class Main2Activity extends AppCompatActivity {
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
       // firebase a  = new firebase();
       // a.writeNewUser();
        setContentView(R.layout.activity_main2);
        setTitle("");
        Bundle args = getIntent().getExtras();
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

      //  changeTabToHome();
        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        int id = profileManager.getID(args.getString(UserAuthen.Column.USERNAME));
        userProfile.setId(id);

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
                                //ChageeTabToParty();
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

    public void ChageeTabToParty(){
        Intent intent = new Intent(context, ChatMainActivity.class);
        context.startActivities(new Intent[]{intent});

//        Party_adapter adapter = new Party_adapter(getSupportFragmentManager(),context);
//        pager.setAdapter(adapter);
//        pager.setCurrentItem(0);
//        tabLayout.setupWithViewPager(pager);
    }



    public  void changeTabToHome(){
        Home_adpater adpater = new Home_adpater(getSupportFragmentManager(),context);
        context.startActivity(intent);
//        MainMenu_adpater adpater = new MainMenu_adpater(getSupportFragmentManager(),context);
//        pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(adpater);
//        pager.setCurrentItem(1);
//        tabLayout.setupWithViewPager(pager);
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
        int id= item.getItemId();
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
        int id = userProfile.getId();
        String User_id = String.valueOf(id);
        switch(menuItem.getItemId()) {
            case R.id.Home:
                Intent Home_intent = new Intent(context, InsertUserProfile.class);
                startActivity(Home_intent);
                break;
            case R.id.NotificationUser:

                break;
            case R.id.UserProfile:
                 int id = userProfile.getId();
                String a = String.valueOf(id);
                Intent intent = new Intent(context, ShowUserprofile.class);
                intent.putExtra(UserProfile.Column.ID,User_id);
                startActivity(intent);
                break;
            case R.id.Favourite:

                break;
            case R.id.History:

                break;
            case R.id.RestaurantProfile:
                Bundle bundle = new Bundle();
                bundle.putString("message",User_id);
                //set Fragmentclass Arguments
                Option_RestaurantFragment option = new Option_RestaurantFragment();
                option.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flContent,option).commit();
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

}
