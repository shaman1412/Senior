package com.Senior.Faff.Promotion;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.Fragment.Party.Join_Recycler;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Promotion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Promotion_recycleview extends RecyclerView.Adapter<Promotion_recycleview.ViewHolder> {

    private Context context;
    private Promotion[] list;
    public String resid;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            Name = (TextView)v.findViewById(R.id.name);
            image = (ImageView)v.findViewById(R.id.image);

    }

    }
    public Promotion_recycleview(Promotion[] list, String resid){
        this.list = list;
        this.resid = resid;

    }

    @Override
    public Promotion_recycleview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_promotion_recycleview, parent, false);
        Promotion_recycleview.ViewHolder vh = new Promotion_recycleview.ViewHolder(view);
        context = view.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final Promotion_recycleview.ViewHolder holder, int position) {
        holder.Name.setText(list[position].getTitle());
        String url = list[position].getPromotionpictureurl().split(",")[0];
        try {
            if(url.contains(" "))
            {
                Picasso.with(context).cancelRequest(holder.image);
                String tmp = url.replaceAll(" ", "%20");
                Picasso.with(context).load(tmp).into(holder.image);
            }
            else
            {
                Picasso.with(context).cancelRequest(holder.image);
                Picasso.with(context).load(url).into(holder.image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}
