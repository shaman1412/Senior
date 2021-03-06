package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Senior.Faff.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Not_Today on 4/20/2017.
 */

public class ShowResRecAdapter extends RecyclerView.Adapter<ShowResRecAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private String[] img_path;
    private int width;

    public ShowResRecAdapter(Context context, String[] img_path, int width) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.img_path = img_path;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list_res_pro, parent, false);
        ShowResRecAdapter.ViewHolder holder = new ShowResRecAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setTag(position);
        String url = img_path[position].split(",")[0];
        try {
            if(url.contains(" "))
            {
                Picasso.with(context).cancelRequest(holder.imageView);
                String tmp = url.replaceAll(" ", "%20");
                Picasso.with(context).load(tmp).fit().into(holder.imageView);
            }
            else
            {
                Picasso.with(context).cancelRequest(holder.imageView);
                Picasso.with(context).load(url).fit().into(holder.imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
