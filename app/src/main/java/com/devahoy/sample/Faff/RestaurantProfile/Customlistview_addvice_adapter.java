package com.devahoy.sample.Faff.RestaurantProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devahoy.sample.Faff.R;
/**
 * Created by InFiNity on 19-Feb-17.
 */

public class Customlistview_addvice_adapter extends ArrayAdapter<String> {
    Context mcontext;
    int res_id;
    String res_name;
    LayoutInflater inflater;
    public Customlistview_addvice_adapter(Context context, int res_id, String res_name) {
        super(context, res_id);
        this.mcontext = context;
        this.res_id = res_id;
        this.res_name = res_name;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int postion, View convertView, ViewGroup parent){
        ImageView imageView = null;
        if(convertView == null){
            imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
            convertView  = inflater.inflate(R.layout.list_view_advice_restaurant,parent,false);
        }
        else{
            imageView = (ImageView)convertView.findViewById(R.id.imageView1);
            async_image asyn1 = (async_image)imageView.getTag(R.id.imageView1);
            if(asyn1 == null){
                asyn1.cancel(true);
            }
        }
        imageView.setImageBitmap(null);
        async_image asyn2 = new async_image(mcontext,imageView,res_id);
        asyn2.execute();
        imageView.setTag(R.id.imageView1, asyn2);

        TextView textView = (TextView)convertView.findViewById(R.id.textView1);
        textView.setText(res_name);

        return convertView;
    }

}
