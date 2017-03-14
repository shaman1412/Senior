package com.Senior.Faff.RestaurantProfile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Senior.Faff.Fragment.MainMenu.MainHome_Fragment;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_RestaurantFragment extends Fragment {


    public Add_RestaurantFragment() {
        // Required empty public constructor
    }

    private String get_user_id;
    private int user_id;
    private int type_food;
    private EditText name,detail,picture;
    private Button location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add__restaurant, container, false);

        String value[] = new String[]{"Food on order","Steak","Pizza","Noodle"};
        Spinner spinner = (Spinner)root.findViewById(R.id.type_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,value);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        get_user_id = getArguments().getString("message");
        user_id = Integer.parseInt(get_user_id);

        name = (EditText)root.findViewById(R.id.addName);
        detail = (EditText)root.findViewById(R.id.addDetail);

        location = (Button)root.findViewById(R.id.addLocation);
        picture = (EditText)root.findViewById(R.id.addPicture);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(getActivity(), RestaurantMapsActivity.class);
                startActivity(intent);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

            switch(pos){
                case 0:
                    type_food = 0;
                case 1:
                    type_food = 1;
                case 2:
                    type_food = 2;
                case 3:
                    type_food = 2;

            }

               /* Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Button addre= (Button)root.findViewById(R.id.add);
        addre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant(name.getText().toString(),picture.getText().toString(),location.getText().toString(),detail.getText().toString(),0,user_id,type_food);
                Restaurant_manager manager = new Restaurant_manager(getActivity());
               long result  = manager.AddRestaurant(restaurant);
                if(result == -1){
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    MainHome_Fragment fragment_home = new MainHome_Fragment();
                    FragmentManager fragmentManager_home = getFragmentManager();
                    fragmentManager_home.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.flContent,fragment_home)
                            .commit();

                }

            }
        });

        return  root;
    }



}
