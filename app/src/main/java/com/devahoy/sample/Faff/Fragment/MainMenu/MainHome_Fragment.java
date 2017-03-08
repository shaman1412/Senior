package com.devahoy.sample.Faff.Fragment.MainMenu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devahoy.sample.Faff.R;



public class MainHome_Fragment extends Fragment  {



    public MainHome_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_content__main, container, false);

        /*Button close  = (Button)rootView.findViewById(R.id.close);
        View.OnClickListener a = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(MainHome_Fragment.this).commit();
            }
        };
        close.setOnClickListener(a);*/


        return  rootView;
    }


}
