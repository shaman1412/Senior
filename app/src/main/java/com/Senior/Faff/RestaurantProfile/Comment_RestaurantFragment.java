package com.Senior.Faff.RestaurantProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Not_Today on 4/6/2017.
 */

public class Comment_RestaurantFragment extends Fragment {

    FirebaseDatabase storage = FirebaseDatabase.getInstance();
    String id;
    DatabaseReference ref;
    DatabaseReference rate;
    private Button add_comment;
    private TextView comment_text;
    private ListView listView;
    private ArrayList<String> list_of_comment = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RatingBar rating_star;
    private TextView score;

    public Comment_RestaurantFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        id = getArguments().getString("id");
        ref = storage.getReference("Restaurant").child("Comment");
        rate = storage.getReference("Restaurant").child("score").child(id);

        View root = inflater.inflate(R.layout.comment_restaurant, container, false);

        add_comment = (Button)root.findViewById(R.id.comment_send);
        comment_text = (TextView)root.findViewById(R.id.comment_text);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_text.getText().toString();
                Map<String, Object> map = new HashMap<String, Object>();
                String tmp = ref.push().getKey();


                map.put("comment", comment);
                ref.child(tmp).updateChildren(map);

                map.clear();
                map.put("id", id);
                ref.child(tmp).updateChildren(map);
            }
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list_of_comment);
        listView = (ListView)root.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> set = new ArrayList<String>();
                ArrayList<String> set1 = new ArrayList<String>();

                Iterator i = dataSnapshot.getChildren().iterator();

                int x=0;

                while (i.hasNext())
                {
                    Iterator j = ((DataSnapshot) i.next()).getChildren().iterator();

                    while (j.hasNext())
                    {
                        if(x%2==0)
                            set.add(((DataSnapshot) j.next()).getValue().toString());
                        else
                            set1.add(((DataSnapshot) j.next()).getValue().toString());
                        x++;
                    }
                }

                list_of_comment.clear();
                list_of_comment.addAll(set);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rating_star = (RatingBar)root.findViewById(R.id.ratingBar);

        rate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    String t = dataSnapshot.getValue().toString();
                    rating_star.setRating(Float.parseFloat(t));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        score = (TextView)root.findViewById(R.id.comment_score);

        rating_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String tmp = String.valueOf(rating);
                score.setText(tmp);
                rate.setValue(tmp);
            }
        });

        return root;
    }


}
