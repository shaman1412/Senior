package com.Senior.sample.Faff.Fragment.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.sample.Faff.R;


public class promotion_restaurant_MainFragment extends Fragment {



    public promotion_restaurant_MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_promotion_restaurant__main, container, false);
       /* Button close = (Button)rootView.findViewById(R.id.close);
        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*getFragmentManager().beginTransaction().remove(promotion_restaurant_MainFragment.this).commit();*//*
            }
        };
        close.setOnClickListener(a);*/

    return rootView;
    }

}
