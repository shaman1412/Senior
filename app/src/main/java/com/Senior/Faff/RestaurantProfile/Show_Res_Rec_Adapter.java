package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Senior.Faff.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Not_Today on 4/20/2017.
 */

public class Show_Res_Rec_Adapter extends RecyclerView.Adapter<Show_Res_Rec_Adapter.ViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private String[] img_path;
    private int width;

    public Show_Res_Rec_Adapter (Context context, String[] img_path, int width) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.img_path = img_path;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list_res_pro, parent, false);
        Show_Res_Rec_Adapter.ViewHolder holder = new Show_Res_Rec_Adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setTag(position);
        if(img_path[position].contains(" "))
        {
            String tmp = img_path[0].replaceAll(" ", "%20");
            Log.i("TEST: ", "image_pth is : "+tmp);
            Picasso.with(context).load(tmp).resize(width, 1080).into(holder.imageView);
        }
        else
        {
            Log.i("TEST: ", "image_pth is : "+img_path[position]);
            Picasso.with(context).load(img_path[position]).resize(width, 1080).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return img_path.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) itemView.findViewById(R.id.image_list_res);
        }

    }
}
