package com.Senior.Faff.RestaurantProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


        Bundle b = new Bundle();
        b.putString("id", strtext);
        Comment_RestaurantFragment cmf = new Comment_RestaurantFragment();
        cmf.setArguments(b);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.comment_restaurant, cmf).commit();

        return  root;
    }

}
