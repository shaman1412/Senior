package com.devahoy.sample.Faff.Fragment.MainMenu;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devahoy.sample.Faff.Fragment.Adapter.Party_adapter;
import com.devahoy.sample.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainParty_fragment extends Fragment {


    public MainParty_fragment() {
        // Required empty public constructor
    }

    private TabLayout tabLayout;
    private ViewPager pager;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_party_fragment, container, false);
        Party_adapter adapter = new Party_adapter(getFragmentManager(),context);
        pager = (ViewPager)root.findViewById(R.id.pager);
        tabLayout = (TabLayout)root.findViewById(R.id.tablayout);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        tabLayout.setupWithViewPager(pager);
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

}
