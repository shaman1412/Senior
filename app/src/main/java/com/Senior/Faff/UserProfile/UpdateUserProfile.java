package com.Senior.Faff.UserProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class UpdateUserProfile extends AppCompatActivity {
    private int genderid;
    private int Userid;
    private EditText name,email,telephone,age,address;
    private Spinner gender,type;
    private Button submit;
    private String userid;
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  ArrayList<String> type_list;
    private String type_check;
    private List_type list_adapter;
    private ArrayList<String> favourite_type;
    private boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        String[] values = {"Femail","Male"};
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mcontext = this;
        type_list = new ArrayList<>();
        name = (EditText)findViewById(R.id.name);

        telephone = (EditText)findViewById(R.id.telephone);
        list_adapter = new List_type();
        address = (EditText)findViewById(R.id.address);

        type = (Spinner)findViewById(R.id.favourite_type);
        submit = (Button)findViewById(R.id.submit);

        favourite_type = new ArrayList<>();
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter_type);


        Bundle arg  = getIntent().getExtras();
        String userid_column = getString(R.string.userid);

        if(arg!=null){
            userid = arg.getString(userid_column);
        }

        new getData().execute(userid);



        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = type.getSelectedItem().toString();
                boolean same = false;
                if(favourite_type!=null) {
                    for (int i = 0; i < favourite_type.size(); i++) {
                        if (text.equals(favourite_type.get(i))) {
                            same = true;
                        }
                    }
                }
                    if (same == false && first == false) {
                        favourite_type.add(text);
                        list_adapter = new List_type(favourite_type, mcontext);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(list_adapter);

                    }
                    if (first == true) {
                        first = false;
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });



        //Userid  = arg.getInt(UserAuthen.Column.ID);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_adapter.getlist() != null) {
                    ArrayList<String> list_get = list_adapter.getlist();
                    for (int i = 0; i < list_get.size(); i++) {
                        if (i == 0) {
                            type_check = list_get.get(i);
                        } else {
                            type_check = type_check + list_get.get(i);
                        }
                        if (i != list_get.size() - 1) {
                            type_check = type_check + ",";
                        }
                    }
                }
                    UserProfile user = new UserProfile(userid, name.getText().toString(), address.getText().toString(), email.getText().toString(), telephone.getText().toString(), type_check, genderid, Integer.parseInt(age.getText().toString()), "asd");
                    new addprofile().execute(user);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class addprofile extends AsyncTask<UserProfile ,String , UserProfile > {

        int responseCode;
        HttpURLConnection connection;
        @Override
        protected UserProfile doInBackground(UserProfile... params) {

            try{

                JSONObject para = new JSONObject();
                para.put(UserProfile.Column.Name, params[0].getName());
                para.put(UserProfile.Column.Address, params[0].getAddress());
                //para.put(UserProfile.Column.Email,params[0].getEmail());
                para.put(UserProfile.Column.Telephone, params[0].getTelephone());
                para.put(UserProfile.Column.Favourite_type, params[0].getFavourite_type());

                String urls = "https://faff-1489402013619.appspot.com/user/" + params[0].getUserid();
                URL url = new URL(urls);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                buffer.write(getPostDataString(para));
                buffer.flush();
                buffer.close();
                out.close();

                responseCode = connection.getResponseCode();

            }catch (Exception e){
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);

                return params[0];
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }
        }

        @Override
        protected void onPostExecute(UserProfile userProfile) {
            if(userProfile != null){
                Toast.makeText(mcontext,type_check,Toast.LENGTH_LONG).show();
                ((Activity)mcontext).finish();
            }
            else{
                Toast.makeText(mcontext,"Fail",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    private class getData extends AsyncTask<String, String, UserProfile> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected UserProfile doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/user/" + args[0];
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
                UserProfile userPro  =  gson.fromJson(result.toString(),  UserProfile.class);
                return userPro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null ;

            }

        }
        @Override
        protected void onPostExecute(UserProfile userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {
                name.setText(userpro.getName());
                address.setText(userpro.getAddress());
                telephone.setText(userpro.getTelephone());
/*                switch (userpro.getGender()){
                    case 0:
                        gender.setSelection(0);
                        break;
                    case 1:
                        gender.setSelection(1);
                        break;
                }*/
                if(userpro.getFavourite_type() != null){
                    String[] list = userpro.getFavourite_type().split(",");
                    for(int i = 0; i < list.length; i++){
                        favourite_type.add(list[i]);
                    }
                    list_adapter = new List_type(favourite_type, mcontext);
                    LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
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
