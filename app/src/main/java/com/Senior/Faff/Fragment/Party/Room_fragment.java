package com.Senior.Faff.Fragment.Party;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Room_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Room_fragment extends Fragment {



    public Room_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_room_fragment, container, false);


        return rootview;
    }

}
