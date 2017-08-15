package com.Senior.Faff.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.Model.PartyChat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Not_Today on 5/1/2017.
 */

public class ChatPartyAdapter extends BaseAdapter {

    ArrayList<PartyChat> list_chat = new ArrayList<>();
    Context mContext;
    String getusername;

    public ChatPartyAdapter(Context context, ArrayList<PartyChat> list_chat, String getusername)
    {
        this.list_chat = list_chat;
        this.mContext = context;
        this.getusername = getusername;

    }

    @Override
    public int getCount() {
        return list_chat.size();
    }

    @Override
    public Object getItem(int position) {

        return list_chat.get(list_chat.size() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_party_chat, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.image_chat);
            holder.username = (TextView) convertView.findViewById(R.id.username_chat_list);
            holder.msg = (TextView) convertView.findViewById(R.id.message_chat_list);

            holder.img2 = (ImageView) convertView.findViewById(R.id.image_chat2);
            holder.username2 = (TextView) convertView.findViewById(R.id.username_chat_list2);
            holder.msg2 = (TextView) convertView.findViewById(R.id.message_chat_list2);


            holder.v1 =  convertView.findViewById(R.id.ownuser);
            holder.v2 =  convertView.findViewById(R.id.no_ownuser);
            convertView.setTag(holder);

        }else{

        holder = (ViewHolder) convertView.getTag();
    }
        final PartyChat list = list_chat.get(position);
        final String path = list.getImage_path();

        if(getusername.equals(list.getUsername())){
            holder.v1.setVisibility(View.VISIBLE);
            holder.v2.setVisibility(View.GONE);
            holder.username2.setText(list.getUsername());
            holder.msg2.setText(list.getMessage());
            try {
                Picasso.with(mContext).cancelRequest(holder.img2);
                Picasso.with(mContext).load(path).resize(120, 120).into(holder.img2);
            }
            catch (Exception  ex)
            {
                ex.printStackTrace();
            }

        }else{
            holder.v2.setVisibility(View.VISIBLE);
            holder.v1.setVisibility(View.GONE);
            holder.username.setText(list.getUsername());
            holder.msg.setText(list.getMessage());
            try {
                Picasso.with(mContext).cancelRequest(holder.img);
                Picasso.with(mContext).load(path).resize(120, 120).into(holder.img);
            }
            catch (Exception  ex)
            {
                ex.printStackTrace();
            }
        }

        return convertView;
    }
    final class ViewHolder {
        public ImageView img;
        public ImageView img2;
        public TextView username2,username;
        public TextView msg,msg2;
        public View v1,v2;

    }

}
