package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class list_party_member extends RecyclerView.Adapter<list_party_member.ViewHolder>{
    private ArrayList<UserProfile> list ;
    private Context context;
    private String key;
    private String  setuserid;
    private  Context mcontext;
    private boolean host;
    public static class ViewHolder extends RecyclerView.ViewHolder {
                // each data item is just a string in this case

        public TextView list_name;
        public ImageView image;
        public Button kick;
        public LinearLayout mix;
        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.name);
            image =  (ImageView)v.findViewById(R.id.image);
            kick = (Button) v.findViewById(R.id.kick);
            mix = (LinearLayout)v.findViewById(R.id.mix);
        }
    }

    public list_party_member(ArrayList<UserProfile> list, Context context, String key, boolean host){
                this.list = list;
                this.context = context;
                this.key = key;
                this.host = host;
            }
            @Override
            public list_party_member.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_list_party_accept, parent, false);
                ViewHolder vh = new ViewHolder(view);
                if(!host) {
                    View ac = view.findViewById(R.id.kick);
                    ac.setVisibility(View.INVISIBLE);
                }
                mcontext = view.getContext();
                return vh;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {

                String url = list.get(position).getPicture();
                String tmp[] = url.split(",");
                if(tmp[0].contains(" "))
                {
                    url = tmp[0].replaceAll(" ", "%20");
                    Log.i("TEST:", " list party member url ");
                    Picasso.with(mcontext).load(url).resize(150, 150).into(holder.image);
                }
                else
                {
                    Log.i("TEST:", " list party member url ");
                    Picasso.with(mcontext).load(tmp[0]).resize(150, 150).into(holder.image);
                }

                holder.list_name.setText(list.get(position).getName());
                holder.mix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mcontext, ShowUserprofile.class);
                        intent.putExtra(UserProfile.Column.UserID,list.get(position).getUserid());
                        mcontext.startActivity(intent);
                    }
                });
                holder.kick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //holder.mix.setVisibility(View.GONE);
                        list.remove(position);
                        notifyDataSetChanged();
                        boolean first = true;
                        boolean change = false;
                        for(int i =0 ;i<list.size(); i++){
                            if(first) {
                                setuserid = list.get(i).getUserid();
                                first =  false;
                                change = true;
                            }
                            else {
                                setuserid = setuserid + ",";
                                setuserid = setuserid + list.get(i).getUserid();
                                change = true;
                            }
                        }
                        if (!change){
                            setuserid = null;
                        }
                        sendRequest(setuserid);


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

    public void sendRequest(String setuserid){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");

        mDatabase.child(key).child("accept").setValue(setuserid);

    }


    public ArrayList<UserProfile> getlist(){
                return  list;
            }
}
