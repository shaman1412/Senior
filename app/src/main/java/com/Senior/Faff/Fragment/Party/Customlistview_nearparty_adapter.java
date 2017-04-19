package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.async_image;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by InFiNity on 16-Mar-17.
 */

public class Customlistview_nearparty_adapter extends ArrayAdapter<Party> {

    Context mcontext;
    int[] room_id;
    ArrayList<Party> room_name;
    LayoutInflater inflater;
    private String type_result;
    private int count =0;
    public Customlistview_nearparty_adapter(Context context,int tv, ArrayList<Party> res_name,int[] res_id) {
        super(context,tv,res_name);
        this.room_id = res_id;
        this.room_name = res_name;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = null;
        Customlistview_nearparty_adapter.ViewHolder viewHolder;
        if(convertView == null){
            convertView  = inflater.inflate(R.layout.list_view_near_party,parent,false);
            imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
            viewHolder = new  Customlistview_nearparty_adapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = ( Customlistview_nearparty_adapter.ViewHolder)convertView.getTag();
            imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
            async_image asyn1 = (async_image)imageView.getTag(R.id.imageView1);
            if(asyn1 == null){
                asyn1.cancel(true);
            }
        }
        //imageView.setImageBitmap(null);
        async_image asyn2 = new async_image(getContext(),imageView,room_id[0]);
        asyn2.execute();
        imageView.setTag(R.id.imageView1, asyn2);

        if(room_name.get(position).getAccept() !=null) {
            String[] people = room_name.get(position).getAccept().split(",");
            count  = people.length;
        }
        viewHolder.RoomName.setText(room_name.get(position).getName());
        viewHolder.appointment.setText(room_name.get(position).getAppointment());
        viewHolder.maxpeople.setText(Integer.toString(room_name.get(position).getPeople()));
        viewHolder.currentpeople.setText(Integer.toString(count));
        viewHolder.detail.setText("     "+room_name.get(position).getDescription());
        return convertView;
    }
    private class ViewHolder {
        public TextView RoomName;
        public TextView appointment;
        public TextView maxpeople;
        public TextView currentpeople;
        public TextView detail;

        public ViewHolder(View convertView) {
            RoomName = (TextView)convertView.findViewById(R.id.textView1);
            appointment = (TextView)convertView.findViewById(R.id.Location_text);
            maxpeople = (TextView)convertView.findViewById(R.id.max);
            currentpeople = (TextView)convertView.findViewById(R.id.people);
            detail = (TextView)convertView.findViewById(R.id.detail);
        }
    }

}
