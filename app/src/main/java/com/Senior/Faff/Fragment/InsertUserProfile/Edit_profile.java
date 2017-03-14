package com.Senior.Faff.Fragment.InsertUserProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.Senior.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_profile extends Fragment {


    public Edit_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View root = inflater.inflate(R.layout.edit_profile, container, false);
        Spinner dropdown = (Spinner)root.findViewById(R.id.spinner1);
        String[] items = new String[]{"Female", "Male"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        return root;
    }

}
