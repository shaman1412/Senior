package com.Senior.Faff.Fragment.Home;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Restaurant_manager;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.LoadingFragment;
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
 */
public class top_resturant_MainFragment extends Fragment {


    public top_resturant_MainFragment() {
        // Required empty public constructor
    }
    private Context mcontext;
    private ListView listview;
    private int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};
    private String userid;
    private static FrameLayout loading;
    private LoadingFragment loadingFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcontext  = getContext();
        View root = inflater.inflate(R.layout.fragment_top_resturant__main, container, false);
        userid = getArguments().getString("userid");
        listview = (ListView)root.findViewById(R.id.listView1);
        loading = (FrameLayout) root.findViewById(R.id.loading);
        new getData().execute();
        return  root;
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoading();
        try {
            loadingFragment = new LoadingFragment();
            getFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getData extends AsyncTask<String, String, Restaurant[] > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected Restaurant[] doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_list/top" ;
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
                Restaurant[] userPro  =  gson.fromJson(result.toString(),  Restaurant[].class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(final Restaurant[] respro) {
            super.onPostExecute(respro);
            if (respro != null) {
                listview.setAdapter( new Customlistview_addvice_adapter(mcontext,0,respro,resId));
                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        Intent intent = new Intent(mcontext, Show_RestaurantProfile.class);
                        intent.putExtra(Restaurant.Column.ResID,respro[position].getresId());
                        intent.putExtra(Restaurant.Column.UserID,respro[position].getUserID());
                        intent.putExtra(UserProfile.Column.UserID, userid);
                        startActivity(intent);
                    }
                });


            }
            else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }

    public static void showLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.GONE)
            {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void hideLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.VISIBLE)
            {
                loading.setVisibility(View.GONE);
            }
        }
    }


}
