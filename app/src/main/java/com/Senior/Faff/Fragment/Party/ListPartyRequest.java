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
import com.Senior.Faff.Model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListPartyRequest extends RecyclerView.Adapter<ListPartyRequest.ViewHolder>{
    private ArrayList<UserProfile> list ;
    private Context context;
    private String key;
    private String  setuserid;
    private String list_accept;
    private  Context mcontext;
    private boolean host;
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

    public ListPartyRequest(ArrayList<UserProfile> list, Context context, String key, String list_accept, boolean host){
        this.list = list;
        this.context = context;
        this.key = key;
        this.list_accept = list_accept;
        this.host = host;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_party_request, parent, false);
            ViewHolder vh = new ViewHolder(view);
            if(!host) {
                View ac = view.findViewById(R.id.accept);
                ac.setVisibility(View.INVISIBLE);
                View re = view.findViewById(R.id.reject);
                re.setVisibility(View.INVISIBLE);
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
            try {
                Picasso.with(mcontext).load(url).resize(150, 150).into(holder.image);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            Log.i("TEST:", " list party member url ");
            try {
                Picasso.with(mcontext).load(tmp[0]).resize(150, 150).into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
