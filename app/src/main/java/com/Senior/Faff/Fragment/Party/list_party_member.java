package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_type;
import com.Senior.Faff.UserProfile.List_typeNodel;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class list_party_member extends RecyclerView.Adapter<list_party_member.ViewHolder>{
    private UserProfile[] list ;
    private Context context;
    private String key;

    public static class ViewHolder extends RecyclerView.ViewHolder {
                // each data item is just a string in this case

        public TextView list_name;
        public ImageView image;
        public Button kick;

        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.name);
            image =  (ImageView)v.findViewById(R.id.image);
            kick = (Button)v.findViewById(R.id.kick);
        }
    }

    public list_party_member(UserProfile[] list, Context context, String key){
                this.list = list;
                this.context = context;
                this.key = key;
            }
            @Override
            public list_party_member.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_list_party, parent, false);
                ViewHolder vh = new ViewHolder(view);

                return vh;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position) {

                holder.list_name.setText(list[position].getName());
                holder.kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest(list[position].getUserid(),list);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return list.length;
            }

            @Override
            public void onAttachedToRecyclerView(RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
            }

    public void sendRequest(String userid,UserProfile[] list){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");
        StringBuilder  setuserid  = new StringBuilder();
        for(int i =0 ;i<list.length; i++){
            if(userid != list[i].getName()){
                setuserid.append(list[i].getName());
            }
            if(i != list.length - 1){
                setuserid.append(",");
            }
        }
        mDatabase.child(key).child("request").setValue(setuserid);

    }
            public UserProfile[] getlist(){
                return  list;
            }



        }
