package com.Senior.Faff.Promotion;

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

public class PromotionRecyclerViewAdapter extends RecyclerView.Adapter<PromotionRecyclerViewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;

    public PromotionRecyclerViewAdapter(Context context,ArrayList<Bitmap> bitmap, ArrayList<Bitmap> bm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_list_picture, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.imageView.setTag(position);
        holder.imageView.setImageBitmap(bitmap.get(position));

        cancel.setTag(position);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Integer index = (Integer) v.getTag();
                PromotionActivity.bmap.remove(position);
                PromotionActivity.imgPath.remove(position);
                PromotionActivity.image_count--;
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