package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link party_member_accept.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class party_member_accept extends Fragment {


    public party_member_accept() {
        // Required empty public constructor
    }
    private RecyclerView mRecyclerView;
    private String group_userid;
    private String userid;
    private ArrayList<String>
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root=  inflater.inflate(R.layout.fragment_party_member_accept, container, false);

      group_userid  = getArguments().getString(UserProfile.Column.UserID);

        mRecyclerView = (RecyclerView)root.findViewById(R.id.mRecyclerView);

        new getData().execute(group_userid);
        return  root;
    }
    private class getData extends AsyncTask<String, String, Restaurant > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected Restaurant doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_profile/" + args[0];
            try {
                URL url = new URL(url_api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                responseCode = connection.getResponseCode();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                Gson gson = new Gson();
                Restaurant userPro  =  gson.fromJson(result.toString(),  Restaurant.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(Restaurant respro) {
            super.onPostExecute(respro);
            if (respro != null) {
                name.setText(respro.getRestaurantName());
                address.setText(respro.getAddress());
                telephone.setText(respro.getTelephone());
                description.setText(respro.getDescription());
                period.setText(respro.getPeriod());
                rate.setRating(respro.getScore());
                text_rate.setText(String.valueOf(respro.getScore()));
                enableMyLocation(respro.getLocation(),respro.getRestaurantName());
                userid = respro.getUserID();
                resid =  respro.getresId();
                setTitle(respro.getRestaurantName());
                if(respro.getTypefood() != null){
                    String[] list = respro.getTypefood().split(",");
                    favourite_type = new ArrayList<String>();
                    for(int i = 0; i < list.length; i++){
                        favourite_type.add(list[i]);
                    }
                    list_adapter = new List_typeNodel(favourite_type, mcontext);
                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);
                }

            }
            else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }



}
