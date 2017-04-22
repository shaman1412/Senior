package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.Fragment.Party.list_party_member;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.model.Bookmark;
import com.Senior.Faff.model.BookmarkList;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class list_bookmark extends RecyclerView.Adapter<list_bookmark.ViewHolder>{
    private ArrayList<Restaurant> list ;
    private Context context;
    private String key;
    private String  setuserid;
    private  Context mcontext;
    private boolean host;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView list_name,detail,open_text;
        public ImageView image;
        public Button kick;
        public LinearLayout mix;
        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.textView1);
            image =  (ImageView)v.findViewById(R.id.image);
            detail = (TextView) v.findViewById(R.id.detail);
            open_text = (TextView)v.findViewById(R.id.open_text);
            mix = (LinearLayout)v.findViewById(R.id.mix);
        }
    }

    public list_bookmark(ArrayList<Restaurant> list, Context context){
        this.list = list;
        this.context = context;
        this.key = key;
        this.host = host;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listview_bookanark_adapter, parent, false);
        ViewHolder vh = new ViewHolder(view);
        mcontext = view.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.list_name.setText(list.get(position).getRestaurantName());
        holder.detail.setText(list.get(position).getDescription());
        holder.open_text.setText(list.get(position).getPeriod());
        holder.mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Show_RestaurantProfile.class);
                intent.putExtra(UserProfile.Column.UserID,list.get(position).getresId());
                mcontext.startActivity(intent);
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


    public ArrayList<Restaurant> getlist(){
        return  list;
    }
}

