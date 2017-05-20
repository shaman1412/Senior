package com.Senior.Faff.RestaurantProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.Senior.Faff.UserProfile.ProfileManager;
import com.Senior.Faff.model.Comment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by Not_Today on 4/6/2017.
 */

public class Comment_RestaurantFragment extends Fragment {

    FirebaseDatabase storage = FirebaseDatabase.getInstance();
    String id;
    DatabaseReference comment;
    private Button add_comment;
    private TextView comment_text;
    private ListView listView;
//    private ArrayList<String> list_of_comment = new ArrayList<>();
    private ArrayList<Comment> list = new ArrayList<>();
    private CommentAdapter adapter;
    private RatingBar rating_star;
    private RatingBar rating_total;
    private TextView score;
    private DatabaseReference rate;
    private String resid;
    private String username;
    private Button enter_comment;

    public Comment_RestaurantFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        id = getArguments().getString("id");
        resid = getArguments().getString("resid");
        username = getArguments().getString("username");
        comment = storage.getReference("Restaurant").child("Comment").child(resid);
        rate = storage.getReference("Restaurant").child("score").child(resid);

        View root = inflater.inflate(R.layout.comment_restaurant, container, false);

//        add_comment = (Button)root.findViewById(R.id.comment_send);
//        comment_text = (TextView)root.findViewById(R.id.comment_text);

//        add_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String c = comment_text.getText().toString();
//                Map<String, Object> map = new HashMap<String, Object>();
//                Map<String, Object> map1 = new HashMap<String, Object>();
//                Map<String, Object> map2 = new HashMap<String, Object>();
//                Map<String, Object> map3 = new HashMap<String, Object>();
//
//                String tmp = comment.push().getKey();
//
//                map.put("id", id);
//
//                map1.put("name", username);
//
//                map2.put("comment", c);
//
//                DateFormat dateformat = DateFormat.getDateTimeInstance();
//                dateformat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//                Date date = new Date();
//                map3.put("date", dateformat.format(date));
//
//                comment.child(tmp).updateChildren(map);
//                comment.child(tmp).updateChildren(map1);
//                comment.child(tmp).updateChildren(map2);
//                comment.child(tmp).updateChildren(map3);
//            }
//        });
        Bundle i = new Bundle();
        i.putString("resid", resid);
        i.putString("id", id);
        i.putString("username", username);

        Add_Comment add_comment = new Add_Comment();
        FragmentManager fm = getFragmentManager();
        add_comment.setArguments(i);
        try {
            fm.beginTransaction().replace(R.id.add_comment, add_comment).commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

 /*
        enter_comment = (Button) root.findViewById(R.id.enter_comment);
        enter_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Add_Comment.class);
                i.putExtra("resid", resid);
                i.putExtra("id", id);
                i.putExtra("username", username);
                startActivity(i);
            }
        });*/

        adapter = new CommentAdapter(getContext(), list);
        listView = (ListView)root.findViewById(R.id.listView);
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        listView.setAdapter(adapter);

        comment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> setId = new ArrayList<String>();
                ArrayList<String> setName = new ArrayList<String>();
                ArrayList<String> setComment = new ArrayList<String>();
                ArrayList<String> setDate = new ArrayList<String>();

                Iterator i = dataSnapshot.getChildren().iterator();

                int x=0;

                while (i.hasNext())
                {
                    Iterator j = ((DataSnapshot) i.next()).getChildren().iterator();

                    while (j.hasNext())
                    {
                        if(x%4==0)
                            setComment.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%4==1)
                            setDate.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%4==2)
                            setId.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%4==3)
                            setName.add(((DataSnapshot) j.next()).getValue().toString());
                        x++;
                    }
                }

//                list_of_comment.clear();
//                list_of_comment.addAll(setComment);

                if(setId.size() == setComment.size() && setId.size() == setDate.size() && setId.size() == setName.size())
                {
                    list.clear();
                    for(int q=0; q<setComment.size();q++)
                    {
                        Comment com = new Comment(setId.get(q),setName.get(q),setComment.get(q),setDate.get(q));
                        list.add(com);
                    }

                    adapter.notifyDataSetChanged();
                    Log.i(Comment_RestaurantFragment.class.getSimpleName(), "size equal : "+setId.size());
                }
                else
                    Log.i(Comment_RestaurantFragment.class.getSimpleName(), "size c d i n : "+setComment.size()+" "+setDate.size()+" "+setId.size()+" "+setName.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //rating_star = (RatingBar)root.findViewById(R.id.ratingBar);
        rating_total = (RatingBar) getActivity().findViewById(R.id.rate);
        score = (TextView) getActivity().findViewById(R.id.text_rate);

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

//                    rating_star.setOnRatingBarChangeListener(null);
                    float scor = sum/n;
                    rating_total.setRating(scor);
                    score.setText(String.valueOf(new DecimalFormat("#.##").format(sum/n)));
//                    rating_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                        @Override
//                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                            String tmp = String.valueOf(rating);
//                            score.setText(tmp);
//                            rate.child(id).setValue(tmp);
//                        }
//                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //score = (TextView)root.findViewById(R.id.comment_score);

/*        rating_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String tmp = String.valueOf(rating);
                score.setText(tmp);
                rate.child(id).setValue(tmp);
            }
        });*/

        return root;
    }

}
