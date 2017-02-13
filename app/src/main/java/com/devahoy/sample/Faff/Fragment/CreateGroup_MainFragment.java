package com.devahoy.sample.Faff.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devahoy.sample.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroup_MainFragment extends Fragment {


    public CreateGroup_MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_group__main, container, false);


        return  rootView;
    }

}
