package com.Senior.Faff.Promotion;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.promotion_view_list;
import com.Senior.Faff.utils.DatabaseManager;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PromotionView extends AppCompatActivity {

    public static final String TAG = PromotionView.class.getSimpleName();
    //    ListView mListPromotion;
    RecyclerView mListPromotion;
    DatabaseManager mManager;
    ArrayList<promotion_view_list> data;
    ArrayList<Bitmap> bmap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        Intent i = getIntent();
        String tmp = i.getExtras().getString("pro");
        Promotion data = new Gson().fromJson(tmp, Promotion.class);

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

        tmp = i.getExtras().getString("path");
        ArrayList<String> path = new Gson().fromJson(tmp, ArrayList.class);
        ArrayList<Bitmap> arr_bmp = new ArrayList<>();
        Helper h = new Helper();
        for(int j=0; j<path.size(); j++)
        {
            arr_bmp.add(h.loadImageFromStorage(path.get(j),data.getTitle()+String.valueOf(j)));
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        PromotionViewImageRecyclerViewAdapter adapter = new PromotionViewImageRecyclerViewAdapter(getApplicationContext(), arr_bmp);
        recyclerView.setAdapter(adapter);

    }
}

