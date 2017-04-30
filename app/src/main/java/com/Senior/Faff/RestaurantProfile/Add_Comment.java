package com.Senior.Faff.RestaurantProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Add_Comment extends AppCompatActivity {

    private Button add_comment;
    private EditText comment_text;
    private DatabaseReference comment;
    FirebaseDatabase storage = FirebaseDatabase.getInstance();
    private String resid;
    private String id;
    private String username;
    private RatingBar rating_star;
    private DatabaseReference rate;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__comment);

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        resid = getIntent().getExtras().getString("resid");
        id = getIntent().getExtras().getString("id");
        username = getIntent().getExtras().getString("username");

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        comment = storage.getReference("Restaurant").child("Comment").child(resid);
        rate = storage.getReference("Restaurant").child("score").child(resid);

        add_comment = (Button) findViewById(R.id.comment_send);
        comment_text = (EditText) findViewById(R.id.comment_text);
        rating_star = (RatingBar) findViewById(R.id.ratingBar);
        score = (TextView) findViewById(R.id.score);

        rating_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String tmp = String.valueOf(rating);
                score.setText(tmp);
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

                finish();
            }
        });


    }
}
