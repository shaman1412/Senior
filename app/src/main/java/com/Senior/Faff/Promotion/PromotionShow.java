package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class PromotionShow extends AppCompatActivity{

    private static final String TAG = PromotionShow.class.getSimpleName();

    private static Context mContext;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_show);

        mContext = this;

        ListPromotion lsp = new ListPromotion(new ListPromotion.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                JSONObject item = new JSONObject(output);
                JSONArray jsarr = item.getJSONArray("items");

                ArrayList<String[]> bitmap_url_list = new ArrayList<>();
                final ArrayList<Promotion> pro_list = new ArrayList<>();

                for(int i=0; i<jsarr.length(); i++)
                {
                    String[] arr_url = jsarr.getJSONObject(i).getString("promotionpictureurl").split(",");
                    bitmap_url_list.add(arr_url);
                    Promotion pro = new Gson().fromJson(jsarr.getJSONObject(i).toString(), Promotion.class);
                    Log.i(TAG, "   interface data from GET is : " + pro.toString());
                    pro_list.add(pro);
                }

                ListView list = (ListView) findViewById(R.id.listView1) ;
                PromotionShowAdapter adapter = new PromotionShowAdapter(PromotionShow.this, pro_list);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Promotion pro = pro_list.get(position);
                        int proid = pro.getId();

                        Intent i = new Intent(getApplicationContext(), PromotionView.class);
                        i.putExtra("id", proid);
                        startActivity(i);
                    }
                });

                //finish();
            }
        });
        lsp.execute("");
//        try {
//            Log.i(TAG, "   main data from GET is : " + lsp.get());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static class ListPromotion extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public AsyncResponse delegate = null;

        public ListPromotion(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list");
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