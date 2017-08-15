package com.Senior.Faff.Fragment.Party;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.Model.Party;
import com.Senior.Faff.Model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomRecycler extends RecyclerView.Adapter<RoomRecycler.ViewHolder>{

    Context mContext;
    Context context;
    ArrayList<Party> list;
    private int count=0;
    private int ccc=0;
    public String userid;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public LinearLayout mix;
        public TextView appointment;
        public TextView maxpeople;
        public TextView RoomName;
        public TextView currentpeople;
        public TextView detail;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView1);
            RoomName = (TextView) v.findViewById(R.id.textView1);
            appointment = (TextView) v.findViewById(R.id.appointment);
            maxpeople = (TextView) v.findViewById(R.id.max);
            currentpeople = (TextView) v.findViewById(R.id.people);
            detail = (TextView) v.findViewById(R.id.detail);
            mix = (LinearLayout) v.findViewById(R.id.mix);
        }
    }

    public RoomRecycler(Context mcontext, ArrayList<Party> room_name, String userid) {
        this.mContext = mcontext;
        this.list = room_name;
        this.userid = userid;
    }


    @Override
    public RoomRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_room__recycler, parent, false);
        ViewHolder vh = new ViewHolder(view);
        context = view.getContext();
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String pic_url = list.get(position).getPicture();
        if (pic_url != null) {
            String[] tmp = pic_url.split("/");
            StorageReference load = FirebaseStorage.getInstance().getReference().child(tmp[1]).child(tmp[2]);

            load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching
                    Log.i("TEST:", " recycler count : "+ccc);
                    ccc++;
                    try
                    {
                        Picasso.with(mContext).cancelRequest(holder.imageView);
                        Picasso.with(mContext).load(uri.toString()).fit().into(holder.imageView);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    Log.i("TEST:", " room recycler uri " + " : " + uri.toString() + " postition : "+position);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    Log.i("TEST: ", " room recycler in List err " +  " : " + exception.toString());
                    exception.printStackTrace();
                }
            });
        }

        if (list.get(position).getAccept() != null) {
            String[] people = list.get(position).getAccept().split(",");
            count = people.length;
        }

        holder.mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowPartyProfile.class);
                intent.putExtra(Party.Column.RoomID, list.get(position).getRoomID());
                intent.putExtra(UserProfile.Column.UserID, userid);
                mContext.startActivity(intent);
            }
        });

        holder.RoomName.setText(list.get(position).getName());
        holder.appointment.setText(list.get(position).getAppointment());
        holder.maxpeople.setText(Integer.toString(list.get(position).getPeople()));
        holder.currentpeople.setText(Integer.toString(count));
        holder.detail.setEllipsize(TextUtils.TruncateAt.END);
        holder.detail.setLines(2);
        holder.detail.setMaxLines(2);
        holder.detail.setText("     " + list.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<Party> getlist(){
        return list;
    }

}
