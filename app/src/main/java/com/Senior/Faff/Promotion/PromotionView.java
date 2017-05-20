package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class PromotionView extends AppCompatActivity {
    public static final String TAG = PromotionView.class.getSimpleName();

    //    ListView mListPromotion;
    static Context mContext;
    private Toolbar toolbar;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_view);

        mContext = this;
        Intent i = getIntent();
       final String id = String.valueOf(i.getExtras().getInt("id"));

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                Log.i(TAG, "  item is : "+item.toString());

                Promotion data = new Gson().fromJson(item.toString(), Promotion.class);
                String[] arr_url = item.getString("promotionpictureurl").split(",");

                TextView textView1 = (TextView) findViewById(R.id.pro_name);
                textView1.setText(data.getTitle());

                TextView textView2 = (TextView) findViewById(R.id.startDate_text);
                textView2.setText(data.getStartDate());

                TextView textView3 = (TextView) findViewById(R.id.endDate_text);
                textView3.setText(data.getEndDate());

                TextView textView4 = (TextView) findViewById(R.id.pro_detail_text);
                textView4.setText(data.getPromotionDetail());


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.promo_image);
                PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(mContext, arr_url);
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);

                TextView edit_pro =(TextView)findViewById(R.id.edit_pro);
                edit_pro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,Edit_promotion.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });
                ////////////////////////////////////////////// Delete ////////////////////////////////////////////////
                TextView delete_pro = (TextView)findViewById(R.id.delete_pro);
                delete_pro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new delete().execute(id);
                    }
                });

            }
        });
        lsp.execute(id);

    }

    private static class ListPromotion extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public PromotionView.ListPromotion.AsyncResponse delegate = null;

        public ListPromotion(PromotionView.ListPromotion.AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/"+Integer.parseInt(params[0]));
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
                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class delete extends AsyncTask<String , Void, Boolean>{
        int responseCode;
        HttpURLConnection connection;
        @Override
        protected Boolean doInBackground(String... params) {
            try{
                JSONObject para = new JSONObject();
                String url_api = "https://faff-1489402013619.appspot.com/res_profile/del/" + params[0];
                URL url = new URL(url_api);
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded" );
                connection.setRequestMethod("DELETE");
                connection.connect();

                responseCode = connection.getResponseCode();

            }catch (Exception e){
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                return true;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(mContext,"Restaurant deleted",Toast.LENGTH_SHORT);
                Intent intent = new Intent(mContext, Main2Activity.class);
                intent.putExtra(UserProfile.Column.UserID,userid);
                startActivity(intent);
            }else{
                Toast.makeText(mContext,"Cant Delete",Toast.LENGTH_SHORT);
            }
        }
    }

}

