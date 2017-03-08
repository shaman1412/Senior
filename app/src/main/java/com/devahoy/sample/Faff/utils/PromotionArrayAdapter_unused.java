package com.devahoy.sample.Faff.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.devahoy.sample.Faff.PromotionActivity;
import com.devahoy.sample.Faff.R;
import com.devahoy.sample.Faff.model.Promotion;

import java.util.ArrayList;


/**
 * Created by Not_Today on 2/11/2017.
 */

public class PromotionArrayAdapter_unused extends ArrayAdapter<Bitmap> {
    private final Activity context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;


    public PromotionArrayAdapter_unused(Activity context, ArrayList<Bitmap> bitmap, ArrayList<Bitmap> bm)
    {
        super(context, R.layout.promotion_list_picture,bm);
        this.context = context;
        this.bitmap = bitmap;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.promotion_list_picture, null, true);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        cancel = (Button) rowView.findViewById(R.id.cancelUpload);

        imageView.setTag(position);
        imageView.setImageBitmap(bitmap.get(position));

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

        return rowView;
    }



}
