package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by InFiNity on 19-Feb-17.
 */

public class Customlistview_addvice_adapter extends ArrayAdapter<Restaurant> {
    Context mcontext;
    int[] res_id;
   Restaurant[] res_name;
    LayoutInflater inflater;
    private String type_result;
    public Customlistview_addvice_adapter(Context context,int tv, Restaurant[] res_name,int[] res_id) {
        super(context,tv,res_name);
        this.res_id = res_id;
         this.res_name = res_name;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = null;
        ViewHolder viewHolder;
        if(convertView == null){
            convertView  = inflater.inflate(R.layout.list_view_advice_restaurant,parent,false);
            imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
            imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
            async_image asyn1 = (async_image)imageView.getTag(R.id.imageView1);
            if(asyn1 == null){
                asyn1.cancel(true);
            }
        }
       //imageView.setImageBitmap(null);
        async_image asyn2 = new async_image(getContext(),imageView,res_id[0]);
        asyn2.execute();
        imageView.setTag(R.id.imageView1, asyn2);


        viewHolder.ResName.setText(res_name[position].getRestaurantName());
        viewHolder.detail.setText( "        "+res_name[position].getDescription());
        viewHolder.period.setText(res_name[position].getPeriod());


        return convertView;
    }
    private class ViewHolder {
        public TextView ResName;
        public TextView detail;
        public TextView period;

        public ViewHolder(View convertView) {
            ResName = (TextView)convertView.findViewById(R.id.textView1);
            detail = (TextView)convertView.findViewById(R.id.detail);
            period = (TextView)convertView.findViewById(R.id.open_text);
        }
    }

}
