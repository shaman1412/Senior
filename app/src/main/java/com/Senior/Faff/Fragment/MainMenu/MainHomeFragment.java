package com.Senior.Faff.Fragment.MainMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.Fragment.Adapter.HomeAdpater;
import com.Senior.Faff.R;



public class MainHomeFragment extends Fragment  {


    public MainHomeFragment() {

    }
    private int setCurrentItem = 0;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        String userid = null;
        if(getArguments()!=null)
        {
            userid  = getArguments().getString("userid");
        }
        View rootView =  inflater.inflate(R.layout.fragment_main_home, container, false);
        tabLayout = (TabLayout)rootView.findViewById(R.id.tablayout);
        pager = (ViewPager)rootView.findViewById(R.id.pager);
        HomeAdpater adpater = new HomeAdpater(getChildFragmentManager(),context,userid);
        pager.setAdapter(adpater);
        pager.setCurrentItem(1);
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
        return  rootView;
    }

}
