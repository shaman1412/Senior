package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class PromotionView extends AppCompatActivity {
    public static final String TAG = PromotionView.class.getSimpleName();

    //    ListView mListPromotion;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        mContext = this;
        Intent i = getIntent();
        String id = String.valueOf(i.getExtras().getInt("id"));

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                Log.i(TAG, "  item is : "+item.toString());

                Promotion data = new Gson().fromJson(item.toString(), Promotion.class);
                String[] arr_url = item.getString("promotionpictureurl").split(",");

                TextView textView1 = (TextView) findViewById(R.id.textView3);
                textView1.setText(data.getTitle());

                TextView textView2 = (TextView) findViewById(R.id.textView4);
                textView2.setText(data.getStartDate());

                TextView textView3 = (TextView) findViewById(R.id.textView5);
                textView3.setText(data.getEndDate());

                TextView textView4 = (TextView) findViewById(R.id.textView6);
                textView4.setText(data.getPromotionDetail());

                TextView textView5 = (TextView) findViewById(R.id.textView7);
                textView5.setText(data.getGoogleMapLink());

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(mContext, arr_url);
                recyclerView.setAdapter(adapter);

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

                result = new Helper().getRequest(url.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                try {
                    delegate.processFinish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

