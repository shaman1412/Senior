package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.RestaurantProfile.Customlistview_addvice_adapter;
import com.Senior.Faff.RestaurantProfile.async_image;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by InFiNity on 16-Mar-17.
 */

public class Customlistview_nearparty_adapter extends BaseAdapter {

    Context mContext;
    ArrayList<Party> room_name;
    private int count=0;
    private int ccc=0;
//    private ImageView imageView;

    public Customlistview_nearparty_adapter(Context mcontext, ArrayList<Party> room_name) {
        this.mContext = mcontext;
        this.room_name = room_name;
    }

    @Override
    public int getCount() {
        return room_name.size();
    }

    @Override
    public Object getItem(int position) {
        return room_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return room_name.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.list_view_near_party, parent, false);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView RoomName = (TextView) convertView.findViewById(R.id.textView1);
        TextView appointment = (TextView) convertView.findViewById(R.id.Location_text);
        TextView maxpeople = (TextView) convertView.findViewById(R.id.max);
        TextView currentpeople = (TextView) convertView.findViewById(R.id.people);
        TextView detail = (TextView) convertView.findViewById(R.id.detail);

        final String pic_url = room_name.get(position).getPicture();
        if (pic_url != null) {
            String[] tmp = pic_url.split("/");
            StorageReference load = FirebaseStorage.getInstance().getReference().child(tmp[1]).child(tmp[2]);

            load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching
                    Log.i("TEST:", " count : "+ccc);
                    ccc++;
                    Picasso.with(mContext).load(uri.toString()).resize(250, 250).into(imageView);
                    Log.i("TEST:", " uri " + " : " + uri.toString() + " postition : "+position);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    Log.i("TEST: ", " in List err " +  " : " + exception.toString());
                    exception.printStackTrace();
                }
            });
        }

        if (room_name.get(position).getAccept() != null) {
            String[] people = room_name.get(position).getAccept().split(",");
            count = people.length;
        }

        RoomName.setText(room_name.get(position).getName());
        appointment.setText(room_name.get(position).getAppointment());
        maxpeople.setText(Integer.toString(room_name.get(position).getPeople()));
        currentpeople.setText(Integer.toString(count));
        detail.setText("     " + room_name.get(position).getDescription());

        return convertView;
    }
}




//public class Customlistview_nearparty_adapter extends ArrayAdapter<Party> {
//
//
//
//    Context mcontext;
//    int[] room_id;
//    ArrayList<Party> room_name;
//    LayoutInflater inflater;
//    private String type_result;
//    private int count = 0;
//    private int i=0;
//    private Customlistview_nearparty_adapter.ViewHolder viewHolder;
//
//    public Customlistview_nearparty_adapter(Context context, int tv, ArrayList<Party> res_name, int[] res_id) {
//        super(context, tv, res_name);
//        this.mcontext = context;
//        this.room_id = res_id;
//        this.room_name = res_name;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        i++;
//        View row = convertView;
//        viewHolder = null;
//
//        if (row == null) {
//            row = inflater.inflate(R.layout.list_view_near_party, parent, false);
//            viewHolder = new Customlistview_nearparty_adapter.ViewHolder(row);
//            row.setTag(viewHolder);
//        } else {
//            viewHolder = (Customlistview_nearparty_adapter.ViewHolder) row.getTag();
//        }
//
//
//        final String pic_url = room_name.get(position).getPicture();
//        if (pic_url != null) {
//            String[] tmp = pic_url.split("/");
//            StorageReference load = FirebaseStorage.getInstance().getReference().child(tmp[1]).child(tmp[2]);
//
//            load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    // Got the download URL for 'users/me/profile.png'
//                    // Pass it to Picasso to download, show in ImageView and caching
//                    Picasso.with(mcontext).load(uri.toString()).resize(250, 250).into(viewHolder.imageView);
//                    Log.i("TEST:", " uri "+ String.valueOf(position) +" name : "+room_name.get(position).getName()+" : "+uri.toString());
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//
//                    Log.i("TEST: ", " in List err "+ pic_url +" uri "+ String.valueOf(position) +" name : "+room_name.get(position).getName()+" : "+exception.toString());
//                    exception.printStackTrace();
//                }
//            });
//        }
//
//
//        if (room_name.get(position).getAccept() != null) {
//            String[] people = room_name.get(position).getAccept().split(",");
//            count = people.length;
//        }
//        viewHolder.RoomName.setText(room_name.get(position).getName());
//        viewHolder.appointment.setText(room_name.get(position).getAppointment());
//        viewHolder.maxpeople.setText(Integer.toString(room_name.get(position).getPeople()));
//        viewHolder.currentpeople.setText(Integer.toString(count));
//        viewHolder.detail.setText("     " + room_name.get(position).getDescription());
//        Log.i("TEST:", " i : "+i);
//        return row;
//    }
//
//    private class ViewHolder {
//        public TextView RoomName;
//        public TextView appointment;
//        public TextView maxpeople;
//        public TextView currentpeople;
//        public TextView detail;
//        public ImageView imageView;
//
//        public ViewHolder(View convertView) {
//            imageView = (ImageView) convertView.findViewById(R.id.imageView1);
//            RoomName = (TextView) convertView.findViewById(R.id.textView1);
//            appointment = (TextView) convertView.findViewById(R.id.Location_text);
//            maxpeople = (TextView) convertView.findViewById(R.id.max);
//            currentpeople = (TextView) convertView.findViewById(R.id.people);
//            detail = (TextView) convertView.findViewById(R.id.detail);
//        }
//    }
//
//}
