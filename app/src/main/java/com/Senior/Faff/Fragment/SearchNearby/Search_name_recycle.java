package com.Senior.Faff.Fragment.SearchNearby;

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

import com.Senior.Faff.Fragment.Party.list_party_member;
import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Show_RestaurantProfile;
import com.Senior.Faff.UserProfile.ShowUserprofile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
        public LinearLayout item;
        public TextView list_name,open,detail;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            item = (LinearLayout) v.findViewById(R.id.item);
            list_name = (TextView)v.findViewById(R.id.textView1);
            image =  (ImageView)v.findViewById(R.id.image);
            open = (TextView) v.findViewById(R.id.open_text);
            detail = (TextView) v.findViewById(R.id.detail);
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
        mcontext = view.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.list_name.setText(list.get(position).getRestaurantName());
        holder.open.setText(list.get(position).getPeriod());
        holder.detail.setText(list.get(position).getDescription());
        String url = list.get(position).getPicture();
        String[] tmp = url.split(",");

        if (tmp[0].contains(" "))
        {
            url = tmp[0].replaceAll(" ", "%20");
            Log.i("TEST:", " url in search nearby : " +url);
            try {
                Picasso.with(mcontext).load(url).resize(250, 250).into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i("TEST:", " url in search nearby : " +tmp[0]);
            try {
                Picasso.with(mcontext).load(tmp[0]).resize(250, 250).into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, Show_RestaurantProfile.class);
                i.putExtra(Restaurant.Column.ResID, list.get(position).getresId());
                i.putExtra(Restaurant.Column.UserID,list.get(position).getUserID());
                mcontext.startActivity(i);
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



    public ArrayList<Restaurant> getlist(){
        return  list;
    }
}

