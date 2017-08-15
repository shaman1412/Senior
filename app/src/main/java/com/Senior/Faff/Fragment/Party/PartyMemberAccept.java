package com.Senior.Faff.Fragment.Party;


import android.content.Context;
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
import com.Senior.Faff.Model.UserProfile;
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
public class PartyMemberAccept extends Fragment {


    public PartyMemberAccept() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private String group_userid;
    private String userid;
    private ArrayList<String> d;
    private ListPartyMember list_adapter;
    private Context mcontext;
    private String key;
    private boolean host;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_party_member_accept2, container, false);
        group_userid  = getArguments().getString(UserProfile.Column.UserID_accept);
        key = getArguments().getString("key");
        host = getArguments().getBoolean("host");
        mRecyclerView = (RecyclerView)root.findViewById(R.id.mRecyclerView1);
        String b = "a1412,apee";
        new getData().execute(group_userid);
        return  root;
    }
    private class getData extends AsyncTask<String, String, ArrayList<UserProfile> > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected ArrayList<UserProfile> doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/party/userid/" + args[0];
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
                UserProfile[] userPro  =  gson.fromJson(result.toString(),  UserProfile[].class);
                ArrayList<UserProfile> getuserpro= new ArrayList<>();
                for(int i = 0; i< userPro.length; i++){
                    getuserpro.add(userPro[i]);
                }
                return getuserpro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(ArrayList<UserProfile> userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {

                list_adapter = new ListPartyMember(userpro, mcontext, key,host );
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                //mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(list_adapter);



            } else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }


}
