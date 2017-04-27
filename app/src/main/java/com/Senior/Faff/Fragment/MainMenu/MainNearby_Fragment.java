package com.Senior.Faff.Fragment.MainMenu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.Fragment.Adapter.Search_adapter;
import com.Senior.Faff.R;

import java.util.ArrayList;

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
        setHasOptionsMenu(true);
        View root =  inflater.inflate(R.layout.fragment_main_nearby_, container, false);
        tabLayout = (TabLayout)root.findViewById(R.id.tablayout);
        pager = (ViewPager)root.findViewById(R.id.pager);
        Bundle bundle = getArguments();
        Search_adapter adapter = new Search_adapter(getChildFragmentManager(), bundle);
        pager.setAdapter(adapter);
        if(bundle != null) {
            pager.setCurrentItem(bundle.getInt("Postition"));
        }else {
            pager.setCurrentItem(0);
        }
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
