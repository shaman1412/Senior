package com.Senior.Faff.Fragment.MainMenu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Senior.Faff.Fragment.Adapter.Party_adapter;
import com.Senior.Faff.Fragment.Party.Party_CreateNewParty;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.TestMapsActivity;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.CustomTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainParty_fragment extends Fragment {

    private static final String Tag = MainParty_fragment.class.getSimpleName();

    public MainParty_fragment() {
        // Required empty public constructor
    }

    private CustomTabLayout tabLayout;
    private ViewPager pager;
    private Context context;
    private String userid;
    private Bundle bundle;
    private int age;
    private int gender;
    private String name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_party, container, false);
        userid = getArguments().getString(UserProfile.Column.UserID);
        gender = getArguments().getInt(UserProfile.Column.Gender);
        age = getArguments().getInt(UserProfile.Column.Age);
        name= getArguments().getString(UserProfile.Column.Name);

        bundle = new Bundle();
        bundle.putString(UserProfile.Column.UserID,userid);
        bundle.putInt(UserProfile.Column.Age,age);
        bundle.putInt(UserProfile.Column.Gender,gender);
        bundle.putString(UserProfile.Column.Name,name);


        Party_adapter adapter = new Party_adapter(getChildFragmentManager(),context,bundle);
        pager = (ViewPager)root.findViewById(R.id.pager);
        tabLayout = (CustomTabLayout)root.findViewById(R.id.tablayout);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);


        Log.i(Tag, "Alert!!!! index of item in Party_adapter is "+pager.getCurrentItem());

        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), Party_CreateNewParty.class);
                intent.putExtra(UserProfile.Column.UserID,userid);
                intent.putExtra(UserProfile.Column.Name,name);
                startActivity(intent);
            }
        });

        tabLayout.setupWithViewPager(pager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                Log.i(Tag, "Alert!!!! index of item in Party_adapter is "+pager.getCurrentItem());
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
