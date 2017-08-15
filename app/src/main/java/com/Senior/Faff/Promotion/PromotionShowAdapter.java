package com.Senior.Faff.Promotion;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Not_Today on 3/17/2017.
 */

public class PromotionShowAdapter extends BaseAdapter{

    public static final String TAG = PromotionShowAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<Promotion> promotions = new ArrayList<>();

    public PromotionShowAdapter(Context mContext, ArrayList<Promotion> promotions) {
        this.mContext = mContext;
        this.promotions = promotions;
    }

    @Override
    public int getCount() {
        return promotions.size();
    }

    @Override
    public Object getItem(int position) {
        return promotions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.listview_row_promotionshow, parent, false);

        TextView textView1 = (TextView)convertView.findViewById(R.id.textView1);
        textView1.setText(promotions.get(position).getTitle());


        TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);

        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setLines(3);
        textView2.setMaxLines(3);
        textView2.setText(promotions.get(position).getPromotionDetail());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView1);
        String url = promotions.get(position).getPromotionpictureurl().split(",")[0];

        try {
            if(url.contains(" "))
            {
                Picasso.with(mContext).cancelRequest(imageView);
                String tmp = url.replaceAll(" ", "%20");
                Picasso.with(mContext).load(tmp).into(imageView);
            }
            else
            {
                Picasso.with(mContext).cancelRequest(imageView);
                Picasso.with(mContext).load(url).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
