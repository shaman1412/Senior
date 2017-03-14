package com.Senior.Faff.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.Senior.Faff.R;

import java.util.ArrayList;

/**
 * Created by Not_Today on 2/13/2017.
 */

public class ImageArrayAdapter extends ArrayAdapter<Bitmap> {
    private final Activity context;
    private final ArrayList<Bitmap> bitmap;


    public ImageArrayAdapter(Activity context, ArrayList<Bitmap> bitmap)
    {
        super(context, R.layout.promotion_list_picture,bitmap);
        this.context = context;
        this.bitmap = bitmap;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.image_list, null, true);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.image_list1);
        imageView.setTag(position);
        imageView.setImageBitmap(bitmap.get(position));
        return rowView;
    }
}
