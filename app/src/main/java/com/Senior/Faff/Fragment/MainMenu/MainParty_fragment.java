package com.Senior.sample.Faff.Fragment.MainMenu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.sample.Faff.Fragment.Adapter.Party_adapter;
import com.Senior.sample.Faff.MapsActivity;
import com.Senior.sample.Faff.R;
import com.Senior.sample.Faff.utils.CustomTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainParty_fragment extends Fragment {


    public MainParty_fragment() {
        // Required empty public constructor
    }

    private CustomTabLayout tabLayout;
    private ViewPager pager;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_party, container, false);
        Party_adapter adapter = new Party_adapter(getChildFragmentManager(),context);
        pager = (ViewPager)root.findViewById(R.id.pager);
        tabLayout = (CustomTabLayout)root.findViewById(R.id.tablayout);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        tabLayout.setupWithViewPager(pager);
        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


    return root;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.dialog_filter).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
