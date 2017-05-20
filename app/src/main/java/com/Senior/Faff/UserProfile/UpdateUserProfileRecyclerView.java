package com.Senior.Faff.UserProfile;

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
 * Created by Not_Today on 5/20/2017.
 */

public class UpdateUserProfileRecyclerView extends RecyclerView.Adapter<UpdateUserProfileRecyclerView.MyViewHolder>{

    private static final String TAG = UpdateUserProfileRecyclerView.class.getSimpleName();

    private LayoutInflater inflater;
    private Context context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;

    public UpdateUserProfileRecyclerView(Context context,ArrayList<Bitmap> bitmap) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public UpdateUserProfileRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_list_picture, parent, false);
        UpdateUserProfileRecyclerView.MyViewHolder holder = new UpdateUserProfileRecyclerView.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UpdateUserProfileRecyclerView.MyViewHolder holder, final int position) {
        ImageView img = holder.imageView;
        img.setTag(position);
        img.setImageBitmap(bitmap.get(position));

        cancel.setTag(position);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Integer index = (Integer) v.getTag();
                UpdateUserProfile.bmap.remove(position);
                UpdateUserProfile.imgPath.remove(position);
                UpdateUserProfile.image_count--;
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