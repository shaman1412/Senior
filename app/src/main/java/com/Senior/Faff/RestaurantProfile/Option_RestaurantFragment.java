package com.Senior.Faff.RestaurantProfile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Senior.Faff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Option_RestaurantFragment extends Fragment {


    public Option_RestaurantFragment() {
        // Required empty public constructor
    }
        
    private Button add,edit,show;
    private String id;
    private Context mconctext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_option_restaurant, container, false);

        add = (Button)root.findViewById(R.id.add_restaurant);
        edit = (Button)root.findViewById(R.id.edit_restaurant);
        show = (Button)root.findViewById(R.id.show_restaurant);
        id = getArguments().getString("userid");
        TextView getdate  = (TextView)root.findViewById(R.id.get);
        getdate.setText(id);
        mconctext =  getContext();

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
             /*   Bundle bundle = new Bundle();
                bundle.putString("message",id);
                Add_RestaurantFragment add_fragment = new Add_RestaurantFragment();
                add_fragment.setArguments(bundle);
                FragmentManager fragmentManager_add = getFragmentManager();
                fragmentManager_add.beginTransaction().replace(R.id.frame,add_fragment).commit();
                add.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                show.setVisibility(View.GONE);*/
                Intent  intent  = new Intent(getActivity(), RestaurantMapsActivity.class);
                intent.putExtra("userid",id);
                startActivity(intent);

            }

        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_RestaurantFragment edit_fragment = new Edit_RestaurantFragment();
                FragmentManager fragmentManager_edit = getFragmentManager();
                fragmentManager_edit.beginTransaction().replace(R.id.frame,edit_fragment).commit();
                add.setVisibility(view.GONE);
                edit.setVisibility(view.GONE);
                show.setVisibility(view.GONE);
            }
        });
        show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("message",id);
                Show_RestaurantFragment show_fragement = new Show_RestaurantFragment();
                show_fragement.setArguments(bundle);
                FragmentManager fragmentManager_show = getFragmentManager();
                fragmentManager_show.beginTransaction().replace(R.id.frame,show_fragement).commit();
                add.setVisibility(view.GONE);
                edit.setVisibility(view.GONE);
                show.setVisibility(view.GONE);
            }
        });






        return root;
    }

}
