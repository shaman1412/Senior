package com.devahoy.sample.Faff.Fragment.MainMenu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devahoy.sample.Faff.R;


public class Search_MainFragment extends Fragment {



    public Search_MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_search__main, container, false);
       /* Button close = (Button)rootView.findViewById(R.id.close);
        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*getFragmentManager().beginTransaction().remove(Search_MainFragment.this).commit();*//*
            }
        };
        close.setOnClickListener(a);*/

    return rootView;
    }

}
