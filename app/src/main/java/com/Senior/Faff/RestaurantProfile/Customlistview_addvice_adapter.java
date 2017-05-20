package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.Fragment.Party.list_party_request;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by InFiNity on 19-Feb-17.
 */

public class Customlistview_addvice_adapter extends ArrayAdapter<Restaurant> {
    Context mcontext;
    int[] res_id;
    Restaurant[] res_name;
    LayoutInflater inflater;
    private String type_result;

    public Customlistview_addvice_adapter(Context context, int tv, Restaurant[] res_name, int[] res_id) {
        super(context, tv, res_name);
        this.res_name = res_name;
        this.mcontext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView imageView = null;
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_advice_restaurant, parent, false);
//            imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            imageView = (ImageView) convertView.findViewById(R.id.image);
        }


        String[] img_path = res_name[position].getPicture().split(",");
//        Picasso.with(getContext()).load(img_path[0]).resize(300, 300).into(imageView);
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                Log.i("TEST:", "Picasso failed : "+exception.toString()+" Uri is : "+uri.toString());
            }
        });
        if(img_path[0].contains(" "))
        {
            String tmp = img_path[0].replaceAll(" ", "%20");
//            builder.build().cancelRequest(viewHolder.imageView);
            try{
                Picasso.with(this.mcontext).cancelRequest(viewHolder.imageView);
                Picasso.with(this.mcontext).load(tmp).fit().into(viewHolder.imageView);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                Picasso.with(this.mcontext).cancelRequest(viewHolder.imageView);
                Picasso.with(this.mcontext).load(img_path[0]).fit().into(viewHolder.imageView);
            }
            catch (Exception e){
                e.printStackTrace();
            }

//            builder.build().cancelRequest(viewHolder.imageView);
//            builder.build().load(img_path[0]).resize(300, 300).into(viewHolder.imageView);
        }

        viewHolder.detail.setText(res_name[position].getDescription());

/*        viewHolder.detail.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.detail.setLines(2);
        viewHolder.detail.setMarqueeRepeatLimit(2);
        viewHolder.detail.setSelected(true);*/

        viewHolder.detail.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.detail.setLines(2);
        viewHolder.detail.setMaxLines(2);

        viewHolder.ResName.setText(res_name[position].getRestaurantName());
        viewHolder.period.setText(res_name[position].getPeriod());
        getRate(res_name[position].getresId(),viewHolder);
        viewHolder.rate.setRating(res_name[position].getScore());
        return convertView;
    }

    private class ViewHolder {
        public TextView ResName;
        public TextView period;
        private ImageView imageView;
        public TextView detail;
        public RatingBar rate;


        public ViewHolder(View convertView) {
            ResName = (TextView) convertView.findViewById(R.id.textView1);
            period = (TextView) convertView.findViewById(R.id.open_text);
            imageView = (ImageView) convertView.findViewById(R.id.image);
            detail = (TextView)convertView.findViewById(R.id.detail);
            rate = (RatingBar)convertView.findViewById(R.id.ratingBar);
        }

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

}
