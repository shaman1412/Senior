package com.Senior.Faff.Promotion;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.promotion_view_list;
import com.Senior.Faff.utils.DatabaseManager;

import java.util.ArrayList;

public class PromotionShow extends AppCompatActivity {

    private static final String Tag = PromotionShow.class.getSimpleName();
    DatabaseManager mManager;
    ArrayList<Bitmap> bmap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_show);

        mManager = new DatabaseManager(this);
        ArrayList<promotion_view_list> data;
        data = getAllPromotion();


        ListView list = (ListView) findViewById(R.id.listView1) ;
        PromotionShowAdapter adapter = new PromotionShowAdapter(this, data);
        list.setAdapter(adapter);
    }

    private ArrayList<promotion_view_list> getAllPromotion(){
        Cursor c = mManager.getAllPromotion();
        String check1="";                           //for keeping previous id
        String check2="";                           //for kepping next id
        ArrayList<promotion_view_list> ls = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                if(c.isFirst()&&c.isLast())
                {
                    byte[] imgByte = c.getBlob(2);
                    bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                    ls.add(l);
                }
                else if(c.isLast()){
                    byte[] imgByte = c.getBlob(2);
                    bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                    ls.add(l);
                }
                else{
                    check1 = c.getString(0);
                    c.moveToNext();
                    check2 = c.getString(0);
                    c.moveToPrevious();
                    if(check1.equals(check2)){
                        byte[] imgByte = c.getBlob(2);
                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    }
                    else{
                        byte[] imgByte = c.getBlob(2);
                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                        promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                        ls.add(l);
                        bmap = new ArrayList<>();
                    }
                }
            } while (c.moveToNext());
            mManager.close();
        }
        return ls;
    }
}
