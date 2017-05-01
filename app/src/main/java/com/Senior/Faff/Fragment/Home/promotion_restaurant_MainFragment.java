package com.Senior.Faff.Fragment.Home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.Promotion.PromotionShow;
import com.Senior.Faff.Promotion.PromotionShowAdapter;
import com.Senior.Faff.Promotion.PromotionView;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class promotion_restaurant_MainFragment extends Fragment {


    private static Context mContext;

    public promotion_restaurant_MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_promotion_restaurant__main, container, false);

        mContext = getActivity();

        promotion_restaurant_MainFragment.ListPromotion lsp = new promotion_restaurant_MainFragment.ListPromotion(new promotion_restaurant_MainFragment.ListPromotion.AsyncResponse() {
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
                    Log.i("TEST:", "   interface data from GET is : " + pro.toString());
                    pro_list.add(pro);
                }

                ListView list = (ListView) rootView.findViewById(R.id.listView1) ;
                PromotionShowAdapter adapter = new PromotionShowAdapter(getActivity(), pro_list);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Promotion pro = pro_list.get(position);
                        int proid = pro.getId();

                        Intent i = new Intent(mContext, PromotionView.class);
                        i.putExtra("id", proid);
                        startActivity(i);
                    }
                });

                //finish();
            }
        });
        lsp.execute("");


       /* Button close = (Button)rootView.findViewById(R.id.close);
        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*getFragmentManager().beginTransaction().remove(promotion_restaurant_MainFragment.this).commit();*//*
            }
        };
        close.setOnClickListener(a);*/

    return rootView;
    }

    private static class ListPromotion extends AsyncTask<String, String, String> {

        String result = "";

        public interface AsyncResponse {
            void processFinish(String output) throws JSONException;
        }

        public promotion_restaurant_MainFragment.ListPromotion.AsyncResponse delegate = null;

        public ListPromotion(promotion_restaurant_MainFragment.ListPromotion.AsyncResponse delegate){
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

}
