package com.Senior.Faff.Fragment.Home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Restaurant_manager;
import com.Senior.Faff.model.Restaurant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class advice_restaurant_MainFragment extends Fragment {


    public advice_restaurant_MainFragment() {
        // Required empty public constructor
    }
    private Context mcontext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};
        mcontext  = getContext();
        Restaurant_manager res_manager = new Restaurant_manager(mcontext);
        Restaurant model = new Restaurant();
        ArrayList<Restaurant> re_list = new ArrayList<>();
       // re_list = res_manager.showAllRestaurant();

        //re_list.get(1).getRestaurantName();
        String[] list = {"ร้านนำ้ ย่านสาทร","ครัวน้าอ้วน","Don Don tei"};
        View rootView = inflater.inflate(R.layout.fragment_advice_restaurant__main, container, false);
        ListView listview = (ListView)rootView.findViewById(R.id.listView1);
        listview.setAdapter( new Customlistview_addvice_adapter(getContext(),0,re_list,resId));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
               if(position == 0){
                   Toast.makeText(getContext(),"position 0",Toast.LENGTH_SHORT).show();
               }
                if(position== 1){
                    Toast.makeText(getContext(),"position 1",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  rootView;
    }

}
