package com.Senior.Faff.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.promotion_view_list;

import java.util.ArrayList;

/**
 * Created by Not_Today on 2/21/2017.
 */

public class PromotionViewRecyclerViewAdapter extends RecyclerView.Adapter<PromotionViewRecyclerViewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<promotion_view_list> promotions;

    public PromotionViewRecyclerViewAdapter(Context context, ArrayList<promotion_view_list> promotions) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.promotions = promotions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_view_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.id.setTag(position);
        holder.title.setTag(position);
        holder.ls.setTag(position);
        holder.startDate.setTag(position);
        holder.endDate.setTag(position);
        holder.detail.setTag(position);
        holder.location.setTag(position);


        holder.id.setText(promotions.get(position).getId());
        PromotionViewImageRecyclerViewAdapter img = new PromotionViewImageRecyclerViewAdapter(context, promotions.get(position).getPicture());
//        ImageArrayAdapter img = new ImageArrayAdapter((Activity) context, promotions.get(position).getPicture());
        holder.ls.setAdapter(img);
        holder.startDate.setText(promotions.get(position).getStartDate());
        holder.endDate.setText(promotions.get(position).getEndDate());
        holder.title.setText(promotions.get(position).getTitle());
        holder.location.setText(promotions.get(position).getLocation());
        holder.detail.setText(promotions.get(position).getPromotionDetail());

    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView id;
        RecyclerView ls;
//        ListView ls;
        TextView title;
        TextView startDate;
        TextView endDate;
        TextView detail;
        TextView location;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.pvl_id);
//            ls = (ListView) itemView.findViewById(R.id.pvl_img);
            ls = (RecyclerView) itemView.findViewById(R.id.pvl_img);
            title = (TextView) itemView.findViewById(R.id.pvl_title);
            startDate = (TextView) itemView.findViewById(R.id.pvl_startDate);
            endDate = (TextView) itemView.findViewById(R.id.pvl_endDate);
            detail = (TextView) itemView.findViewById(R.id.pvl_detail);
            location = (TextView) itemView.findViewById(R.id.pvl_location);
        }
    }
}
