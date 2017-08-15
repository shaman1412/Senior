package com.Senior.Faff.Chat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.PartyChat;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class ChatParty extends AppCompatActivity {

    private ImageView btn_send_msg;
    private EditText input_msg;
    private String user_name;
    private String room_name;
    private TextView room;
    private ImageView room_image;
    private String image_path;
    private Context mcontext;
    private DatabaseReference root;
    private String temp_key;
    private ListView listview;
    ArrayList<PartyChat> list_chat = new ArrayList<>();
    ChatPartyAdapter adapter;
    private String image_path_user;
    private String userid;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__party);

        mcontext = this;
        userid = getIntent().getExtras().getString(UserProfile.Column.UserID);

        ///////////////////////////////     Note        //////////////////////////////

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        image_path = getIntent().getExtras().get("room_image").toString();
        image_path_user = getIntent().getExtras().get("user_image").toString();

        ///////////////////////////////     Note        //////////////////////////////

        btn_send_msg = (ImageView) findViewById(R.id.send);
        input_msg = (EditText) findViewById(R.id.input);

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ChatPartyAdapter(mcontext, list_chat,user_name);
        listview.setAdapter(adapter);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(room_name);


        ///////////////////////////////     Note        //////////////////////////////


 /*       room = (TextView) findViewById(R.id.room_name);
        room.setText(room_name);*/

     /*   room_image = (ImageView) findViewById(R.id.room_image);
        try
        {
            Picasso.with(mcontext).load(image_path).resize(150,150).into(room_image);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }*/

        root = FirebaseDatabase.getInstance().getReference("ChatRoom").child(room_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();

                DateFormat dateformat = DateFormat.getDateTimeInstance();
                dateformat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                Date date = new Date();
                map2.put("date", dateformat.format(date));

                map2.put("name",user_name);
                map2.put("msg", input_msg.getText().toString());
                map2.put("image_path", image_path_user);

                message_root.updateChildren(map2);
                input_msg.setText(null);
            }
        });

        root.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateList(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateList(DataSnapshot dataSnapshot){

        String image_path,chat_msg, chat_user_name, date_time;

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            date_time = (String) ((DataSnapshot)i.next()).getValue();                //message
            image_path = (String) ((DataSnapshot)i.next()).getValue();              //image
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();               //date
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();          //name

            list_chat.add(new PartyChat(chat_user_name, image_path, chat_msg, date_time));
            adapter.notifyDataSetChanged();
            listview.setSelection(adapter.getCount() - 1);

        }

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

}
