package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.Senior.Faff.model.promotion_view_list;
import com.Senior.Faff.utils.DatabaseManager;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PromotionShow extends AppCompatActivity {

    private static final String TAG = PromotionShow.class.getSimpleName();

    private Context mContext;
    DatabaseManager mManager;
    ArrayList<Bitmap> bmap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_show);

        mContext = this;

        ListPromotion lsp = new ListPromotion();
        lsp.execute("");
        String dat = lsp.getResult();
        Log.i(TAG, "  data from GET is : "+dat);

//        final ArrayList<promotion_view_list> data;
//        data = getAllPromotion();
//
//
//        ListView list = (ListView) findViewById(R.id.listView1) ;
//        PromotionShowAdapter adapter = new PromotionShowAdapter(this, data);
//        list.setAdapter(adapter);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                promotion_view_list tmp = data.get(position);
//                Helper h = new Helper();
//                ArrayList<String> path = new ArrayList<String>();
//                for(int i=0; i<tmp.getPicture().size(); i++)
//                {
//                    String txt = h.saveToInternalStorage(tmp.getPicture().get(i), getApplicationContext(), tmp.getTitle()+ i);
//                    path.add(txt);
//                    Log.i(TAG, "ID is " + tmp.getId().toString() + "    Path: "+ txt);
//                }
//                Promotion pro = new Promotion(tmp.getTitle(),"","", tmp.getStartDate(), tmp.getEndDate(), tmp.getPromotionDetail(), tmp.getLocation());
//                pro.setId(Integer.parseInt(tmp.getId()));
//
//                Intent i = new Intent(getApplicationContext(), PromotionView.class);
//                String tmp_object = new Gson().toJson(pro);
//                i.putExtra("pro", tmp_object);
//                String tmp_object1 = new Gson().toJson(path);
//                i.putExtra("path", tmp_object1);
//                startActivity(i);
//            }
//        });

    }

    private class ListPromotion extends AsyncTask<String, String, String> {

        private ArrayList<Promotion> promotion_list_return = new ArrayList<>();
        String result = "";

        public String getResult() {
            return result;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list");
                //URL url = new URL("http://localhost:8080/promotion_list");

                result = new Helper().getRequest(url.toString());
                Log.i(TAG, "result for getRequest(url.toString()  :  " + result);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                finish();
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private ArrayList<promotion_view_list> getAllPromotion(){
//        Cursor c = mManager.getAllPromotion();
//        String check1="";                           //for keeping previous id
//        String check2="";                           //for kepping next id
//        ArrayList<promotion_view_list> ls = new ArrayList<>();
//        if(c.moveToFirst()) {
//            do {
//                if(c.isFirst()&&c.isLast())
//                {
//                    byte[] imgByte = c.getBlob(2);
//                    bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
//                    promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
//                    ls.add(l);
//                }
//                else if(c.isLast()){
//                    byte[] imgByte = c.getBlob(2);
//                    bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
//                    promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
//                    ls.add(l);
//                }
//                else{
//                    check1 = c.getString(0);
//                    c.moveToNext();
//                    check2 = c.getString(0);
//                    c.moveToPrevious();
//                    if(check1.equals(check2)){
//                        byte[] imgByte = c.getBlob(2);
//                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
//                    }
//                    else{
//                        byte[] imgByte = c.getBlob(2);
//                        bmap.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
//                        promotion_view_list l = new promotion_view_list(c.getString(0),c.getString(1),bmap,c.getString(3),c.getString(4),c.getString(5),c.getString(6));
//                        ls.add(l);
//                        bmap = new ArrayList<>();
//                    }
//                }
//            } while (c.moveToNext());
//            mManager.close();
//        }
//        return ls;
//    }
}
