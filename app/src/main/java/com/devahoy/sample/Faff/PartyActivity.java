package com.devahoy.sample.Faff;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.devahoy.sample.Faff.Fragment.Adapter.Party_adapter;
import com.devahoy.sample.Faff.model.Party;

public class PartyActivity extends AppCompatActivity {
    private ViewPager pager;
    private Context context;
    private TabLayout tablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        context = this;

        Party_adapter adapter = new Party_adapter(getSupportFragmentManager(),context);
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);

        tablayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tablayout.setupWithViewPager(pager);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
