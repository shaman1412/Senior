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
 * Created by Not_Today on 5/21/2017.
 */

public class EditPromotionRecyclerView extends RecyclerView.Adapter<EditPromotionRecyclerView.MyViewHolder>{

    private static final String TAG = EditPromotionRecyclerView.class.getSimpleName();

    private LayoutInflater inflater;
    private Context context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;

    public EditPromotionRecyclerView(Context context, ArrayList<Bitmap> bitmap) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public EditPromotionRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_list_picture, parent, false);
        EditPromotionRecyclerView.MyViewHolder holder = new EditPromotionRecyclerView.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditPromotionRecyclerView.MyViewHolder holder, final int position) {
        ImageView img = holder.imageView;
        img.setTag(position);
        img.setImageBitmap(bitmap.get(position));

        cancel.setTag(position);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Integer index = (Integer) v.getTag();
                EditPromotion.bmap.remove(position);
                EditPromotion.imgPath.remove(position);
                EditPromotion.image_count--;
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