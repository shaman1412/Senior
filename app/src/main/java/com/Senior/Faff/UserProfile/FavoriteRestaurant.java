package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.Model.BookmarkList;
import com.Senior.Faff.Model.Restaurant;
import com.Senior.Faff.Model.UserProfile;
import com.Senior.Faff.utils.LoadingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FavoriteRestaurant extends AppCompatActivity {
    private String userid;
    private ListView listview;
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private ListBookmark list_adapter;
    private String key;
    private BookmarkList book;
    private Toolbar toolbar;

    private static FrameLayout loading;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Favourite restaurant");


        BookmarkList bookmark = new BookmarkList();
        Bundle args = getIntent().getExtras();
        mcontext = this;
        if (args != null) {
            userid = args.getString(UserProfile.Column.UserID);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView1);
        mRecyclerView.bringToFront();
        loading = (FrameLayout) findViewById(R.id.loading);
        hideLoading();
    }

    private class getBookmark extends AsyncTask<String, String, Void> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;

        @Override
        protected Void doInBackground(String... args) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("All_Bookmark");
            mRootRef.orderByChild("userID")
                    .equalTo(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        showLoading();
                        loadingFragment = new LoadingFragment();
                        ((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        key = postSnapshot.getKey();
                        book = postSnapshot.getValue(BookmarkList.class);
                        new getData().execute(book.getBookmarkID());
                    }


                    //showlist(listview, party_list, resId,gender,age);
                    //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Cant connect database",Toast.LENGTH_SHORT).show();
                }

            });
            return null;
        }

    }

    private class getData extends AsyncTask<String, String, ArrayList<Restaurant>> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;

        @Override
        protected ArrayList<Restaurant> doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/res_list/" + args[0];
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
                Restaurant[] userPro = gson.fromJson(result.toString(), Restaurant[].class);
                ArrayList<Restaurant> getuserpro = new ArrayList<>();
                for (int i = 0; i < userPro.length; i++) {
                    getuserpro.add(userPro[i]);
                }
                return getuserpro;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }

        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {

                list_adapter = new ListBookmark(userpro, mcontext, userid);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(list_adapter);


            } else {
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
    protected void onResume() {
        super.onResume();
        new getBookmark().execute();
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
