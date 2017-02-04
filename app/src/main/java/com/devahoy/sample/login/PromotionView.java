package com.devahoy.sample.login;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devahoy.sample.login.model.Promotion;
import com.devahoy.sample.login.utils.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class PromotionView extends ActionBarActivity {

    ListView mListPromotion;
    DatabaseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        mManager = new DatabaseManager(this);

        ArrayList<String> data = getAllPromotion();
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        mListPromotion = (ListView) findViewById(R.id.listview);
        mListPromotion.setAdapter(adapter);

    }

    private ArrayList<String> getAllPromotion(){
        Cursor c = mManager.getAllPromotion();
        ArrayList<String> ls = new ArrayList<String>();
        int i=0;
        if(c.moveToFirst()) {
            do {
                String str = Promotion.Column.ID + ": " + c.getString(0) + "\n" +
                        Promotion.Column.Title + ": " + c.getString(1) + "\n" +
                        Promotion.Column.TitlePicture + ": " + c.getString(2) + "\n" +
                        Promotion.Column.StartDate + ": " + c.getString(3) + "\n" +
                        Promotion.Column.EndDate + ": " + c.getString(4) + "\n" +
                        Promotion.Column.PromotionDetail + ": " + c.getString(5) + "\n" +
                        Promotion.Column.Location + ": " + c.getString(6) + "\n\n\n";
                ls.add(i,str);
                i++;
                //Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
            mManager.close();
            //t.setText(ls.toString());
        }
        return ls;
    }
}
