package com.devahoy.sample.Faff.UserProfile;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.devahoy.sample.Faff.Fragment.Adapter.InsertUserProfile_adapter;
import com.devahoy.sample.Faff.R;

public class InsertUserProfile extends AppCompatActivity {
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user_profile);

         pager = (ViewPager)findViewById(R.id.pager);
       InsertUserProfile_adapter adapter = new InsertUserProfile_adapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
    }
}
