package com.Senior.Faff.Fragment.SearchNearby;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;
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
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    EditText search_name;
    TextView search_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_, container, false);
        search_button = (TextView)root.findViewById(R.id.serach_button);
        search_name = (EditText)root.findViewById(R.id.search_edit);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_name != null) {
                    new search_name().execute(search_name.getText().toString());
                }
                else{
                    Toast.makeText(getContext(),"Restaurant's name wasn't found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public class search_name extends AsyncTask<String, Void, ArrayList<Restaurant> >{

        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            HttpURLConnection connection;
            int responseCode = 0;
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_list/search_name/" + params[0];

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
                Restaurant[] restaurant = gson.fromJson(result.toString(), Restaurant[].class);
                ArrayList<Restaurant> getrestaurant= new ArrayList<>();
                for(int i = 0; i< restaurant.length; i++){
                    getrestaurant.add(restaurant[i]);
                }
                return getrestaurant;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurant) {
            super.onPostExecute(restaurant);
            if(restaurant.size() != 0){
                SearchNameRecycle recycle = new SearchNameRecycle(restaurant);
                RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.search_re);
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(getContext());
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(recycle);
                //Toast.makeText(getContext(),"found",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(),"Restaurant's name wasn't found",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
