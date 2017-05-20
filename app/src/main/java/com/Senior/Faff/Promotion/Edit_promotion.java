package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class Edit_promotion extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promotion);

        mContext = this;
        Intent i = getIntent();
        String id = i.getExtras().getString("id");

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);

                //Log.i(TAG, "  item is : "+item.toString());

                Promotion data = new Gson().fromJson(item.toString(), Promotion.class);
                String[] arr_url = item.getString("promotionpictureurl").split(",");

                EditText textView1 = (EditText) findViewById(R.id.title);
                textView1.setText(data.getTitle());

                EditText textView2 = (EditText) findViewById(R.id.startDate);
                textView2.setText(data.getStartDate());

                EditText textView3 = (EditText) findViewById(R.id.endDate);
                textView3.setText(data.getEndDate());

                EditText textView4 = (EditText) findViewById(R.id.promotionDetail);
                textView4.setText(data.getPromotionDetail());


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlist1);
                PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(mContext, arr_url);
                LinearLayoutManager mLayoutManager  = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
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

        public Edit_promotion.ListPromotion.AsyncResponse delegate = null;

        public ListPromotion(Edit_promotion.ListPromotion.AsyncResponse delegate){
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
}
