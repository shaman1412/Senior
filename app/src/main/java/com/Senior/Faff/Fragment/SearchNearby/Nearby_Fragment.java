package com.Senior.sample.Faff.Fragment.SearchNearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.sample.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby_Fragment extends Fragment {


    public Nearby_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_nearby_, container, false);

        return root;
    }

}
