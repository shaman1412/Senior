package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.ShowRestaurantProfile;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class ListBookmark extends RecyclerView.Adapter<ListBookmark.ViewHolder>{
    private ArrayList<Restaurant> list ;
    private Context context;
    private String key;
    private String  userid;
    private  Context mcontext;
    private boolean host;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView list_name,detail,open_text;
        public ImageView image;
        public Button kick;
        public LinearLayout mix;
        public RatingBar rate;
        public ViewHolder(View v) {
            super(v);
            list_name = (TextView)v.findViewById(R.id.textView1);
            image =  (ImageView)v.findViewById(R.id.image);
            detail = (TextView) v.findViewById(R.id.detail);
            open_text = (TextView)v.findViewById(R.id.open_text);
            mix = (LinearLayout)v.findViewById(R.id.mix);
            rate = (RatingBar)v.findViewById(R.id.ratingBar);

        }
    }

    public ListBookmark(ArrayList<Restaurant> list, Context context, String userid){
        this.list = list;
        this.context = context;
        this.userid = userid;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listview_bookanark_adapter, parent, false);
        ViewHolder vh = new ViewHolder(view);
        mcontext = view.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.list_name.setText(list.get(position).getRestaurantName());
        holder.detail.setText(list.get(position).getDescription());
        holder.open_text.setText(list.get(position).getPeriod());
        getRate(list.get(position).getresId(),holder);
        String url = list.get(position).getPicture();
        String[] tmp = url.split(",");
        if(tmp[0].contains(" "))
        {
            url = tmp[0].replaceAll(" ", "%20");
            Log.i("TEST:", " bookmark url is : "+url);
            try {
                Picasso.with(this.context).load(url).fit().into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i("TEST:", " bookmark url is : "+tmp[0]);
            try {
                Picasso.with(this.context).load(tmp[0]).fit().into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ShowRestaurantProfile.class);
                intent.putExtra(Restaurant.Column.ResID,list.get(position).getresId());
                intent.putExtra(UserProfile.Column.UserID,userid);
                mcontext.startActivity(intent);
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

    public void sendRequest(String setuserid){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Room");

        mDatabase.child(key).child("accept").setValue(setuserid);

    }

    public void getRate(String res_id, final ViewHolder holder){
        DatabaseReference rate;
        FirebaseDatabase storage = FirebaseDatabase.getInstance();
        rate = storage.getReference("Restaurant").child("score").child(res_id);
        rate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    Iterator i = dataSnapshot.getChildren().iterator();
                    float sum = 0;
                    long n = dataSnapshot.getChildrenCount();

                    while (i.hasNext())
                    {
                        String t = ((DataSnapshot) i.next()).getValue().toString();
                        float tmp = Float.parseFloat(t);
                        sum+=tmp;
                    }

                    float scor = sum/n;
                    holder.rate.setRating(scor);
                    // score.setText(String.valueOf(new DecimalFormat("#.##").format(sum/n)));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public ArrayList<Restaurant> getlist(){
        return  list;
    }
}

