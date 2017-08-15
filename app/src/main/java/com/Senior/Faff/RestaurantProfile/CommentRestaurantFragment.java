package com.Senior.Faff.RestaurantProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.Model.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Not_Today on 4/6/2017.
 */

public class CommentRestaurantFragment extends Fragment {

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

    public CommentRestaurantFragment(){

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

        AddComment add_comment = new AddComment();
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
                Intent i = new Intent(getActivity(), AddComment.class);
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
                ArrayList<String> setImage = new ArrayList<String>();

                Iterator i = dataSnapshot.getChildren().iterator();

                int x=0;

                while (i.hasNext())
                {
                    Iterator j = ((DataSnapshot) i.next()).getChildren().iterator();

                    while (j.hasNext())
                    {
                        if(x%5==0)
                            setComment.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%5==1)
                            setDate.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%5==2)
                            setId.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%5==3)
                            setName.add(((DataSnapshot) j.next()).getValue().toString());
                        else if(x%5==4)
                            setImage.add(((DataSnapshot) j.next()).getValue().toString());
                        x++;
                    }
                }

//                list_of_comment.clear();
//                list_of_comment.addAll(setComment);

                if(setId.size() == setComment.size() && setId.size() == setDate.size() && setId.size() == setName.size() && setId.size() == setImage.size())
                {
                    list.clear();
                    for(int q=0; q<setComment.size();q++)
                    {
                        Comment com = new Comment(setId.get(q),setName.get(q),setComment.get(q),setDate.get(q), setImage.get(q));
                        list.add(com);
                    }

                    adapter.notifyDataSetChanged();
                    Log.i(CommentRestaurantFragment.class.getSimpleName(), "size equal : "+setId.size());
                }
                else
                    Log.i(CommentRestaurantFragment.class.getSimpleName(), "size c d i n : "+setComment.size()+" "+setDate.size()+" "+setId.size()+" "+setName.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //rating_star = (RatingBar)root.findViewById(R.id.ratingBar);
        rating_total = (RatingBar) getActivity().findViewById(R.id.rate);

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
