package com.Senior.Faff.Fragment.SearchNearby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.Fragment.Party.list_party_member;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by InFiNity on 01-May-17.
 */

public class Search_name_recycle extends RecyclerView.Adapter<Search_name_recycle.ViewHolder>{
    private ArrayList<Restaurant> list ;
    private Context context;
    private String key;
    private String  setuserid;
    private  Context mcontext;
    private boolean host;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView list_name,open;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.textView1);
            image =  (ImageView)v.findViewById(R.id.image);
            open = (TextView) v.findViewById(R.id.open_text);

        }
    }

    public Search_name_recycle(ArrayList<Restaurant> list){
        this.list = list;
    }
    @Override
    public Search_name_recycle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_recycle_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.list_name.setText(list.get(position).getRestaurantName());
        holder.open.setText(list.get(position).getPeriod());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public ArrayList<Restaurant> getlist(){
        return  list;
    }
}

