package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Comment;

import java.util.ArrayList;

/**
 * Created by Not_Today on 4/6/2017.
 */

public class CommentAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Comment> comment = new ArrayList<>();

    public CommentAdapter(Context mcontext, ArrayList<Comment> comment) {
        this.mContext = mcontext;
        this.comment = comment;
    }

    @Override
    public int getCount() {
        return comment.size();
    }

    @Override
    public Object getItem(int position) {
        return comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comment.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.comment_box, parent, false);

        TextView username = (TextView)convertView.findViewById(R.id.comment_username);
        TextView commenttext = (TextView)convertView.findViewById(R.id.comment_text);
        TextView datetime = (TextView)convertView.findViewById(R.id.comment_date_time);

        username.setText(comment.get(position).getName().toString());
        commenttext.setText(comment.get(position).getComment().toString());
        datetime.setText(comment.get(position).getDateTime().toString());

        return convertView;
    }
}
