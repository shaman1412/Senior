package com.Senior.Faff.RestaurantProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Senior.Faff.R;

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

        View root = inflater.inflate(R.layout.fragment_show__restaurant, container, false);


        String strtext= getArguments().getString("message");
        TextView getdate  = (TextView)root.findViewById(R.id.getid);
        getdate.setText(strtext);
    return  root;
    }

}
