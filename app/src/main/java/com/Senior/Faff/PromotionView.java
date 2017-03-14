package com.Senior.sample.Faff;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.Senior.sample.Faff.model.promotion_view_list;
import com.Senior.sample.Faff.utils.DatabaseManager;
import com.Senior.sample.Faff.utils.PromotionViewArrayAdapter;

import java.util.ArrayList;

public class PromotionView extends ActionBarActivity {

    public static final String TAG = PromotionView.class.getSimpleName();
    ListView mListPromotion;
    DatabaseManager mManager;
    ArrayList<promotion_view_list> data;
    ArrayList<Bitmap> bmap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        mManager = new DatabaseManager(this);
        data = getAllPromotion();

        //custom list view

        mListPromotion = (ListView) findViewById(R.id.listview);
        PromotionViewArrayAdapter adapter = new PromotionViewArrayAdapter(this, data);
        mListPromotion.setAdapter(adapter);

    }

    private ArrayList<promotion_view_list> getAllPromotion(){
        Cursor c = mManager.getAllPromotion();
        String check="";
        int i=0;
        ArrayList<promotion_view_list> ls = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                if(c.getString(0).equals(check))
                {
                    if(c.isLast())
                    {
                        byte[] imgByte = c.getBlob(2);
                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                        promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                        ls.add(l);
                        bmap = new ArrayList<>();
                    }
                    else {
                        byte[] imgByte = c.getBlob(2);
                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    }
                }
                else
                {
                    if(!c.isFirst())
                    {
                        if(c.isLast())
                        {
                            promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                            ls.add(l);
                            bmap = new ArrayList<>();
                        }
                        else
                        {
                            c.moveToPrevious();
                            promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                            ls.add(l);
                            bmap = new ArrayList<>();

                            c.moveToNext();
                            byte[] imgByte = c.getBlob(2);
                            bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                        }
                    }
                    else
                    {
                        byte[] imgByte = c.getBlob(2);
                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    }
                }
                check = c.getString(0);
            } while (c.moveToNext());
            mManager.close();
        }
        return ls;
    }
}

