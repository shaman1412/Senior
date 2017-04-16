package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class list_party_request extends RecyclerView.Adapter<list_party_request.ViewHolder>{
    private ArrayList<UserProfile> list ;
    private Context context;
    private String key;
    private String  setuserid;
    private String list_accept;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView list_name;
        public ImageView image;
        public Button accept,reject;
        public LinearLayout mix;
        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.name);
            image =  (ImageView)v.findViewById(R.id.image);
            accept = (Button)v.findViewById(R.id.accept);
            reject = (Button)v.findViewById(R.id.reject);
            mix = (LinearLayout)v.findViewById(R.id.mix);
        }
    }

    public list_party_request(ArrayList<UserProfile> list, Context context, String key, String list_accept){
        this.list = list;
        this.context = context;
        this.key = key;
        this.list_accept = list_accept;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_party_request, parent, false);
       ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.list_name.setText(list.get(position).getName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.mix.setVisibility(View.GONE);
                if(list_accept != null) {
                    list_accept = list_accept + "," + list.get(position).getUserid();
                }
                else{
                    list_accept = list.get(position).getUserid();
                }
                list.remove(position);
                notifyDataSetChanged();
                boolean first = true;
                for(int i =0 ;i<list.size(); i++){
                    if(first) {
                        setuserid = list.get(i).getUserid();
                        first =  false;
                    }
                    else {
                        setuserid = setuserid + ",";
                        setuserid = setuserid + list.get(i).getUserid();
                    }
                }

                sendAccept(setuserid,list_accept);


            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.mix.setVisibility(View.GONE);
                list.remove(position);
                notifyDataSetChanged();
                boolean first = true;
                for(int i =0 ;i<list.size(); i++){
                    if(first) {
                        setuserid = list.get(i).getUserid();
                        first =  false;
                    }
                    else {
                        setuserid = setuserid + ",";
                        setuserid = setuserid + list.get(i).getUserid();
                    }
                }


                sendReject(setuserid);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void sendAccept(String setuserid,String list_accept){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");

        mDatabase.child(key).child("request").setValue(setuserid);
        mDatabase.child(key).child("accept").setValue(list_accept);

    }
    public void sendReject(String setuserid){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");

        mDatabase.child(key).child("request").setValue(setuserid);

    }

    public ArrayList<UserProfile> getlist(){
        return  list;
    }



}
