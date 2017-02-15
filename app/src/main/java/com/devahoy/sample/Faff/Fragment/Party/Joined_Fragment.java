package com.devahoy.sample.Faff.Fragment.Party;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devahoy.sample.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Joined_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Joined_Fragment extends Fragment {



    public Joined_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_joined_, container, false);
    return  rootview;
    }


}
