package com.Senior.Faff.Promotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Senior.Faff.R;

import java.util.ArrayList;

/**
 * Created by Not_Today on 2/21/2017.
 */

public class PromotionViewImageRecyclerViewAdapter extends RecyclerView.Adapter<PromotionViewImageRecyclerViewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Bitmap> bitmap;

    public PromotionViewImageRecyclerViewAdapter (Context context, ArrayList<Bitmap> bitmap) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public PromotionViewImageRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list, parent, false);
        PromotionViewImageRecyclerViewAdapter.MyViewHolder holder = new PromotionViewImageRecyclerViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PromotionViewImageRecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.imageView.setTag(position);
        holder.imageView.setImageBitmap(bitmap.get(position));

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
            imageView = (ImageView) itemView.findViewById(R.id.image_list1);
        }
    }
}
