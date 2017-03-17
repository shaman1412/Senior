package com.Senior.Faff.Promotion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.Senior.Faff.Promotion.PromotionActivity;
import com.Senior.Faff.R;

import java.util.ArrayList;


/**
 * Created by Not_Today on 2/11/2017.
 */

public class PromotionArrayAdapter extends ArrayAdapter<Bitmap> {
    private final Activity context;
    private final ArrayList<Bitmap> bitmap;
    private Button cancel;


    public PromotionArrayAdapter(Activity context, ArrayList<Bitmap> bitmap, ArrayList<Bitmap> bm)
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
