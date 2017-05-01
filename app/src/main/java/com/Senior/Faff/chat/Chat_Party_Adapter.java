package com.Senior.Faff.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.PartyChat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Not_Today on 5/1/2017.
 */

public class Chat_Party_Adapter extends BaseAdapter {

    ArrayList<PartyChat> list_chat = new ArrayList<>();
    Context mContext;

    public Chat_Party_Adapter(Context context, ArrayList<PartyChat> list_chat)
    {
        this.list_chat = list_chat;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list_chat.size();
    }

    @Override
    public Object getItem(int position) {
        return list_chat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.list_view_party_chat, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.image_chat);
        TextView username = (TextView) convertView.findViewById(R.id.username_chat_list);
        TextView msg = (TextView) convertView.findViewById(R.id.message_chat_list);
        TextView date_time = (TextView) convertView.findViewById(R.id.date_time_chat_list);

        String path = list_chat.get(position).getImage_path();
        try {
            Picasso.with(mContext).cancelRequest(img);
            Picasso.with(mContext).load(path).resize(120, 120).into(img);
        }
        catch (Exception  ex)
        {
            ex.printStackTrace();
        }

        username.setText(list_chat.get(position).getUsername());
        msg.setText(list_chat.get(position).getMessage());
        date_time.setText(list_chat.get(position).getDate_time());

        return convertView;
    }
}
