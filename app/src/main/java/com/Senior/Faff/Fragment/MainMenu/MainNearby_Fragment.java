package com.Senior.Faff.Fragment.MainMenu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.Fragment.Adapter.Search_adapter;
import com.Senior.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainNearby_Fragment extends Fragment {


    public MainNearby_Fragment() {
        // Required empty public constructor
    }

    private TabLayout tabLayout;
    private ViewPager pager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_main_nearby_, container, false);
        tabLayout = (TabLayout)root.findViewById(R.id.tablayout);
        pager = (ViewPager)root.findViewById(R.id.pager);
        Search_adapter adapter = new Search_adapter(getChildFragmentManager());
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
