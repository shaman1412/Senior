package com.Senior.Faff.Fragment.Home;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.CustomlistviewAddviceAdapter;
import com.Senior.Faff.RestaurantProfile.ShowRestaurantProfile;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.Model.UserProfile;
import com.Senior.Faff.utils.LoadingFragment;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceRestaurantMainFragment extends Fragment {

    public AdviceRestaurantMainFragment() {
        // Required empty public constructor
    }
    private Context mcontext;
    private ListView listview;
    private String userid;
    private int[] resId =  {R.drawable.restaurant1,R.drawable.restaurant2,R.drawable.restaurant3};

    private static FrameLayout loading;
    private LoadingFragment loadingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcontext  = getContext();
        userid = getArguments().getString("userid");
        View rootView = inflater.inflate(R.layout.fragment_advice_restaurant__main, container, false);
        listview = (ListView)rootView.findViewById(R.id.listView1);

        loading = (FrameLayout)rootView.findViewById(R.id.loading);

        return  rootView;
    }

    private class getData extends AsyncTask<String, String, Restaurant[] > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        String result;

        @Override
        protected Restaurant[] doInBackground(String... args) {
            try {
                showLoading();
                loadingFragment = new LoadingFragment();
                ((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_list/all" ;
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
//                try {
//
//                    URL url = new URL("https://faff-1489402013619.appspot.com/res_list/all");
//                    //URL url = new URL("http://localhost:8080/promotion_list");
//                    result = new Helper().getRequest(url.toString());
//                    Log.i("TEST: ", "  result is : "+result+"  uid is : "+userid);
//
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }

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

//            Gson gson = new Gson();
//            Restaurant[] userPro  =  gson.fromJson(result.toString(),  Restaurant[].class);
//            return userPro;
        }
        @Override
        protected void onPostExecute(final Restaurant[] respro) {
            super.onPostExecute(respro);
            if (respro != null) {
                listview.setAdapter( new CustomlistviewAddviceAdapter(mcontext,0,respro,resId));
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        Intent intent = new Intent(mcontext, ShowRestaurantProfile.class);
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

            if(loadingFragment!=null)
            {
                loadingFragment.onStop();
                hideLoading();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getData().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadingFragment!=null)
        {
            loadingFragment.onStop();
            hideLoading();
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
