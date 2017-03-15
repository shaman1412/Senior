package com.Senior.Faff.Fragment.Party;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Senior.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Created_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Created_Fragment extends Fragment {

    private static final String Tag = Created_Fragment.class.getSimpleName();
    private Button addRoom;

    public Created_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_created_, container, false);
        addRoom = (Button) rootview.findViewById(R.id.AddRoomButton);
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tag, "Click add room!!!");
            }
        });

    return rootview;
    }


}
