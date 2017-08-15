package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.UserProfile;
import com.squareup.picasso.Picasso;

public class PromotionRecycleview extends RecyclerView.Adapter<PromotionRecycleview.ViewHolder> {

    private Context context;
    private Promotion[] list;
    public String resid;
    public String userid;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public ImageView image;
        public LinearLayout getpromotion;
        public ViewHolder(View v) {
            super(v);
            Name = (TextView)v.findViewById(R.id.name);
            image = (ImageView)v.findViewById(R.id.image);
            getpromotion = (LinearLayout)v.findViewById(R.id.getpromotion);

    }

    }
    public PromotionRecycleview(Promotion[] list, String resid, String userid){
        this.list = list;
        this.resid = resid;
        this.userid = userid;

    }

    @Override
    public PromotionRecycleview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_promotion_recycleview, parent, false);
        PromotionRecycleview.ViewHolder vh = new PromotionRecycleview.ViewHolder(view);
        context = view.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final PromotionRecycleview.ViewHolder holder, final int position) {
        holder.Name.setEllipsize(TextUtils.TruncateAt.END);
        holder.Name.setLines(2);
        holder.Name.setMaxLines(2);
        holder.Name.setText(list[position].getTitle());
        String url = list[position].getPromotionpictureurl().split(",")[0];
        try {
            if(url.contains(" "))
            {
                Picasso.with(context).cancelRequest(holder.image);
                String tmp = url.replaceAll(" ", "%20");
                Picasso.with(context).load(tmp).fit().into(holder.image);
            }
            else
            {
                Picasso.with(context).cancelRequest(holder.image);
                Picasso.with(context).load(url).fit().into(holder.image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.getpromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PromotionView.class);
                intent.putExtra("id",list[position].getId());
                intent.putExtra(UserProfile.Column.UserID,userid);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}
