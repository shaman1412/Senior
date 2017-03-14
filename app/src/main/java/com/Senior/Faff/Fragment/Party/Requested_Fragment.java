package com.Senior.sample.Faff.Fragment.Party;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.sample.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Requested_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Requested_Fragment extends Fragment {



    public Requested_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_requested_, container, false);
    return rootview;
    }


}
