package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.Senior.Faff.R;

import java.util.ArrayList;
import java.util.List;

public class List_type  extends RecyclerView.Adapter<List_type.ViewHolder>{
  private   ArrayList<String> list ;
  private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public Button type_list;
        public ViewHolder(View v) {
            super(v);

            type_list = (Button ) v.findViewById(R.id.type_list);
        }
    }

    public List_type(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_type, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder  holder, final int position) {

        holder.type_list.setText(list.get(position));
       holder.type_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


   /* @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public List_type.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater layout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layout.inflate(R.layout.activity_list_type, null);
        }

        Button type_list = (Button)view.findViewById(R.id.type_list);
        type_list.setText(list.get(position));
        type_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });



        return  view;
    }*/
    public ArrayList<String> getlist(){
        return  list;
    }

}
