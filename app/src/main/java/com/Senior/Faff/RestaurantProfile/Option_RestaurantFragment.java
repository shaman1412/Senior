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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.Fragment.Home.advice_restaurant_MainFragment;
import com.Senior.Faff.Fragment.Party.Show_party_profile;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Option_RestaurantFragment extends Fragment {


    public Option_RestaurantFragment() {
        // Required empty public constructor
    }
        
    private Button add,edit,show;
    private String userid;
    private Context mconctext;
    View root;
    private LinearLayout object_set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_option_restaurant, container, false);

        object_set = (LinearLayout)root.findViewById(R.id.object_set);

        add = (Button)root.findViewById(R.id.add_restaurant);
        edit = (Button)root.findViewById(R.id.edit_restaurant);
        show = (Button)root.findViewById(R.id.show_restaurant);
        userid = getArguments().getString("userid");
        TextView getdate  = (TextView)root.findViewById(R.id.get);
        getdate.setText(userid);
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
                Intent  intent  = new Intent(getActivity(),Add_RestaurantProfile.class);
                intent.putExtra(UserProfile.Column.UserID,userid);
                startActivity(intent);

            }

        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Edit_RestaurantFragment edit_fragment = new Edit_RestaurantFragment();
                FragmentManager fragmentManager_edit = getFragmentManager();
                fragmentManager_edit.beginTransaction().replace(R.id.frame,edit_fragment).commit();
                add.setVisibility(view.GONE);
                edit.setVisibility(view.GONE);
                show.setVisibility(view.GONE);*/
                Intent  intent  = new Intent(getActivity(),Show_party_profile.class);
                intent.putExtra(UserProfile.Column.UserID,userid);
                startActivity(intent);

            }
        });
        show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Bundle bundle = new Bundle();
                bundle.putString(UserProfile.Column.UserID,userid);
                advice_restaurant_MainFragment ad = new advice_restaurant_MainFragment();
                ad.setArguments(bundle);
                FragmentManager fragmentManager_show = getFragmentManager();
                fragmentManager_show.beginTransaction().replace(R.id.frame, ad).addToBackStack("frag_option_restaurant").commit();
                object_set.setVisibility(View.GONE);

//                Show_RestaurantFragment show_fragement = new Show_RestaurantFragment();
//                show_fragement.setArguments(bundle);
//                FragmentManager fragmentManager_show = getFragmentManager();
//                fragmentManager_show.beginTransaction().replace(R.id.frame,show_fragement).commit();
//                Intent  intent  = new Intent(getActivity(),Show_RestaurantProfile.class);
//                intent.putExtra(UserProfile.Column.UserID,userid);
//                startActivity(intent);

            }
        });






        return root;
    }

}
