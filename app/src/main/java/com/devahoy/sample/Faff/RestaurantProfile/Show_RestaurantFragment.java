package com.devahoy.sample.Faff.RestaurantProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devahoy.sample.login.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Show_RestaurantFragment extends Fragment {


    public Show_RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show__restaurant, container, false);
    }

}
