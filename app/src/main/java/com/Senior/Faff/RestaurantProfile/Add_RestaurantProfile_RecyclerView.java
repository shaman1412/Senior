package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.Senior.Faff.R;

import java.util.ArrayList;
/**
 * Created by Not_Today on 2/20/2017.
 */

public class Add_RestaurantProfile_RecyclerView extends RecyclerView.Adapter<com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.MyViewHolder>{

    private static final String TAG = com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.class.getSimpleName();

    private LayoutInflater inflater;
    private Context context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;

    public Add_RestaurantProfile_RecyclerView(Context context,ArrayList<Bitmap> bitmap) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_list_picture, parent, false);
        com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.MyViewHolder holder = new com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(com.Senior.Faff.RestaurantProfile.Add_RestaurantProfile_RecyclerView.MyViewHolder holder, final int position) {
        ImageView img = holder.imageView;
        img.setTag(position);
        img.setImageBitmap(bitmap.get(position));

        cancel.setTag(position);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Integer index = (Integer) v.getTag();
                Add_RestaurantProfile.bmap.remove(position);
                Add_RestaurantProfile.imgPath.remove(position);
                Add_RestaurantProfile.image_count--;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bitmap.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            cancel = (Button) itemView.findViewById(R.id.cancelUpload);
        }
    }
}