package com.Senior.Faff.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.promotion_view_list;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Not_Today on 2/13/2017.
 */

public class PromotionViewArrayAdapter extends ArrayAdapter<promotion_view_list> {
    private final Activity context;
    private final ArrayList<promotion_view_list> promotions;

    public PromotionViewArrayAdapter(Activity context, ArrayList<promotion_view_list> promotions) {
        super(context, R.layout.promotion_view_list, promotions);
        this.context = context;
        this.promotions = promotions;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.promotion_view_list, null, true);

        final TextView id = (TextView) rowView.findViewById(R.id.pvl_id);
        final RecyclerView ls = (RecyclerView) rowView.findViewById(R.id.pvl_img);
        final TextView title = (TextView) rowView.findViewById(R.id.pvl_title);
        final TextView startDate = (TextView) rowView.findViewById(R.id.pvl_startDate);
        final TextView endDate = (TextView) rowView.findViewById(R.id.pvl_endDate);
        final TextView detail = (TextView) rowView.findViewById(R.id.pvl_detail);
        final TextView location = (TextView) rowView.findViewById(R.id.pvl_location);

        id.setText(promotions.get(position).getId());
        PromotionViewImageRecyclerViewAdapter img = new PromotionViewImageRecyclerViewAdapter(context, promotions.get(position).getPicture());
        //ImageArrayAdapter img = new ImageArrayAdapter(context, promotions.get(position).getPicture());
        ls.setNestedScrollingEnabled(false);
        ls.setAdapter(img);
        title.setText(promotions.get(position).getTitle());
        startDate.setText(promotions.get(position).getStartDate());
        endDate.setText(promotions.get(position).getEndDate());
        detail.setText(promotions.get(position).getPromotionDetail());
        location.setText(promotions.get(position).getLocation());

        return rowView;
    }
}
