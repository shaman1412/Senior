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
    ArrayList<Restaurant> res_name;
    LayoutInflater inflater;
    private String type_result;
    public Customlistview_addvice_adapter(Context context,int tv, ArrayList<Restaurant> res_name,int[] res_id) {
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


        viewHolder.ResName.setText(res_name.get(position).getRestaurantName());
        viewHolder.ResType.setText(changeTypeResID(res_name.get(position).getTypeID()));
        viewHolder.ResLo.setText(res_name.get(position).getLocation());

        return convertView;
    }
    private class ViewHolder {
        public TextView ResName;
        public TextView ResType;
        public TextView ResLo;

        public ViewHolder(View convertView) {
           ResName = (TextView)convertView.findViewById(R.id.textView1);
            ResType = (TextView)convertView.findViewById(R.id.type_text);
            ResLo = (TextView)convertView.findViewById(R.id.Location_text);
        }
    }
    public String changeTypeResID(int typeID) {
        switch (typeID) {
            case (0):
                type_result = "Food on Order";
                break;
            case (1):
                type_result = "Steak";
                break;
            case (2):
                type_result = "Pizza";
                break;
            case (3):
                type_result = "Noodle";
                break;
        }
        return type_result;
    }

}
