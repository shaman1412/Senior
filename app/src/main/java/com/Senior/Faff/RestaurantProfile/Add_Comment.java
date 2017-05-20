package com.Senior.Faff.RestaurantProfile;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Add_Comment extends Fragment {

    private Button add_comment;
    private EditText comment_text;
    private DatabaseReference comment;
    FirebaseDatabase storage = FirebaseDatabase.getInstance();
    private String resid;
    private String id;
    private String username;
    private RatingBar rating_star;
    private DatabaseReference rate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add__comment, container, false);

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        resid = getArguments().getString("resid");
        id = getArguments().getString("id");
        username = getArguments().getString("username");

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        comment = storage.getReference("Restaurant").child("Comment").child(resid);
        rate = storage.getReference("Restaurant").child("score").child(resid);
        TextView setusername = (TextView)root.findViewById(R.id.username);
        setusername.setText(username);
        add_comment = (Button) root.findViewById(R.id.comment_send);
        comment_text = (EditText) root.findViewById(R.id.comment_text);
        rating_star = (RatingBar) root.findViewById(R.id.ratingBar);
        rating_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String tmp = String.valueOf(rating);
               // score.setText(tmp);
            }
        });

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = comment_text.getText().toString();
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> map1 = new HashMap<String, Object>();
                Map<String, Object> map2 = new HashMap<String, Object>();
                Map<String, Object> map3 = new HashMap<String, Object>();

                String tmp = comment.push().getKey();

                map.put("id", id);

                map1.put("name", username);

                map2.put("comment", c);

                DateFormat dateformat = DateFormat.getDateTimeInstance();
                dateformat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                Date date = new Date();
                map3.put("date", dateformat.format(date));

                comment.child(tmp).updateChildren(map);
                comment.child(tmp).updateChildren(map1);
                comment.child(tmp).updateChildren(map2);
                comment.child(tmp).updateChildren(map3);

                float tmpRating = rating_star.getRating();

                rate.child(id).setValue(String.valueOf(tmpRating));

                comment_text.setText(null);
                rating_star.setRating(0);
               // finish();
            }
        });

        return  root;
    }

}
