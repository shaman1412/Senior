package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.Fragment.Party.list_party_member;
import com.Senior.Faff.Fragment.Party.list_party_request;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Bookmark;
import com.Senior.Faff.model.BookmarkList;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
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
import java.util.List;

public class favorite_restaurant extends AppCompatActivity {
    private String userid;
    private ListView listview;
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private list_bookmark list_adapter;
    private String key;
    private BookmarkList book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_restaurant);

        BookmarkList bookmark = new BookmarkList();
        Bundle args = getIntent().getExtras();
        mcontext = this;
        if(args != null) {
        userid   = args.getString(UserProfile.Column.UserID);
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView1);

    }
    private class getDataMember extends AsyncTask<String, String, BookmarkList > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        BookmarkList bookmark;
        @Override
        protected BookmarkList doInBackground(String... args) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("All_Bookmark");
            mRootRef.orderByChild("userID")
                    .equalTo(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot dataSnapshot){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                       // key = postSnapshot.getKey();
                        bookmark= postSnapshot.getValue(BookmarkList.class);

                    }


                    //showlist(listview, party_list, resId,gender,age);
                    //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Cant connect database",Toast.LENGTH_SHORT).show();
                }

            });
            return bookmark;
        }
        @Override
        protected void onPostExecute(final BookmarkList bookmark) {
            super.onPostExecute(bookmark);
            if (bookmark != null) {
                if(!bookmark.getBookmarkID().isEmpty()) {
                    new getBookmark().execute(bookmark.getBookmarkID());
                }
            }
            else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void setvalue(BookmarkList bookmark) {
        if (!(bookmark.getBookmarkID().isEmpty())) {
            String[] booklist = bookmark.getBookmarkID().split(",");

        }
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
                public void onDataChange (DataSnapshot dataSnapshot){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
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

    private class getData extends AsyncTask<String, String, ArrayList<BookmarkList> > {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;
        @Override
        protected ArrayList<BookmarkList> doInBackground(String... args) {
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
                Restaurant[] userPro  =  gson.fromJson(result.toString(),  Restaurant[].class);
                ArrayList<BookmarkList> getuserpro= new ArrayList<>();
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
        protected void onPostExecute(ArrayList<BookmarkList> userpro) {
            super.onPostExecute(userpro);
            if (userpro != null) {

                list_adapter = new list_bookmark(userpro, mcontext);
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mcontext);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(list_adapter);



            } else{
                String message = getString(R.string.login_error_message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }

        }

    }


}
