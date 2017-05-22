package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Add_Comment extends Fragment {

    Context mcontext;
    private Button add_comment;
    private EditText comment_text;
    private DatabaseReference comment;
    FirebaseDatabase storage = FirebaseDatabase.getInstance();
    private String resid;
    private String id;
    private String username;
    private String picture;
    private RatingBar rating_star;
    private DatabaseReference rate;
    private ImageView comment_photo;
    private TextView setusername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add__comment, container, false);

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        resid = getArguments().getString("resid");
        id = getArguments().getString("id");
        username = getArguments().getString("username");

        /////////////////////////////////////////////////////   note    ///////////////////////////////

        mcontext = getContext();
        comment = storage.getReference("Restaurant").child("Comment").child(resid);
        rate = storage.getReference("Restaurant").child("score").child(resid);
        setusername = (TextView)root.findViewById(R.id.username);
        //setusername.setText(username);
        add_comment = (Button) root.findViewById(R.id.comment_send);
        comment_text = (EditText) root.findViewById(R.id.comment_text);
        rating_star = (RatingBar) root.findViewById(R.id.ratingBar);
        comment_photo = (ImageView) root.findViewById(R.id.comment_photo);

        Add_Comment.GetUser getuser = new Add_Comment.GetUser(new Add_Comment.GetUser.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                try {
                    JSONObject item = new JSONObject(output);
                    UserProfile user = new Gson().fromJson(item.toString(), UserProfile.class);

                    Log.i("TEST:", "user in add comment : "+user.toString());

                    picture = user.getPicture().toString();
                    Picasso.with(mcontext).load(picture).fit().into(comment_photo);
                    setusername.setText(user.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getuser.execute(id);

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
                Map<String, Object> map4 = new HashMap<String, Object>();


                String tmp = comment.push().getKey();

                map.put("id", id);

                map1.put("name", username);

                map2.put("comment", c);

                DateFormat dateformat = DateFormat.getDateTimeInstance();
                dateformat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                Date date = new Date();
                map3.put("date", dateformat.format(date));

                map4.put("picture", picture);

                comment.child(tmp).updateChildren(map);
                comment.child(tmp).updateChildren(map1);
                comment.child(tmp).updateChildren(map2);
                comment.child(tmp).updateChildren(map3);
                comment.child(tmp).updateChildren(map4);

                float tmpRating = rating_star.getRating();

                rate.child(id).setValue(String.valueOf(tmpRating));

                comment_text.setText(null);
                rating_star.setRating(0);
               // finish();
            }
        });

        return  root;
    }

    private static class GetUser extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public Add_Comment.GetUser.AsyncResponse delegate = null;

        public GetUser(Add_Comment.GetUser.AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.i("TEST:", "  params [0] is : "+params[0]);
                URL url = new URL("https://faff-1489402013619.appspot.com/user/"+params[0]);
                //URL url = new URL("http://localhost:8080/promotion_list");

                Helper hp = new Helper();
                hp.setRequest_method("GET");
                result = hp.getRequest(url.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                //Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
