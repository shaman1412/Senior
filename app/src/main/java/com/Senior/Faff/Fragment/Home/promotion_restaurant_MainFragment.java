package com.Senior.Faff.Fragment.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Senior.Faff.Interface.FragmentPagerAdapterInterface;
import com.Senior.Faff.Promotion.PromotionShow;
import com.Senior.Faff.Promotion.PromotionShowAdapter;
import com.Senior.Faff.Promotion.PromotionView;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.Helper;
import com.Senior.Faff.utils.LoadingFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class promotion_restaurant_MainFragment extends Fragment implements FragmentPagerAdapterInterface {

    private static FrameLayout loading;
    private static LoadingFragment loadingFragment;
    private static Context mContext;
    private String userid;
    private View rootView;
    private ListView list;

    public promotion_restaurant_MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_promotion_restaurant__main, container, false);
        userid = getArguments().getString(UserProfile.Column.UserID);

        list = (ListView) rootView.findViewById(R.id.listView1) ;
        loading = (FrameLayout)rootView.findViewById(R.id.loading);
        list.bringToFront();

        mContext = getContext();

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

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

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
                try {
                    showLoading();
                    loadingFragment = new LoadingFragment();
                    ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list");
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
    public void onResume() {
        super.onResume();

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

                PromotionShowAdapter adapter = new PromotionShowAdapter(mContext, pro_list);
                list.setAdapter(adapter);
                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Promotion pro = pro_list.get(position);
                        String proid = pro.getId();

                        Intent i = new Intent(mContext, PromotionView.class);
                        i.putExtra(UserProfile.Column.UserID,userid);
                        i.putExtra("id", proid);
                        startActivity(i);
                    }
                });

                if(loadingFragment!=null)
                {
                    loadingFragment.onStop();
                    hideLoading();
                }

                //finish();
            }
        });
        lsp.execute("");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadingFragment!=null)
        {
            loadingFragment.onStop();
            hideLoading();
        }
    }

    public static void showLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.GONE)
            {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void hideLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.VISIBLE)
            {
                loading.setVisibility(View.GONE);
            }
        }
    }

}
