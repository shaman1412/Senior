package com.devahoy.sample.Faff.UserProfile;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.devahoy.sample.Faff.R;
import com.devahoy.sample.Faff.model.UserProfile;
import com.devahoy.sample.Faff.utils.BitmapImageManager;

public class ShowUserprofile extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView text;
    private ProfileManager profileManager;
    private UserProfile userProfile;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_userprofile);
        setTitle("Profile");
        Bundle args = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        text = (TextView) findViewById(R.id.text);
        String id = args.getString(UserProfile.Column.ID);
        profileManager = new ProfileManager(this);
        userProfile = new UserProfile();
        //  String json = profileManager.getUserProfile(id);
        // text.setText(json);
        BitmapImageManager bitmap = new BitmapImageManager();
        ImageView image = (ImageView) findViewById(R.id.profile_image);
        image.setImageBitmap(bitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.woman, 100, 100));

        final NestedScrollView childScroll = (NestedScrollView) findViewById(R.id.scroll);


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
