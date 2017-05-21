package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

public class Edit_promotion extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private Button uploadPicture;
    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
//    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data

    public static int image_count = 0;                                    //number of images
    private Edit_promotion_RecyclerView adapter;

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

                uploadPicture = (Button) findViewById(R.id.upload_button);

                uploadPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //sdintent.setType("image/*");
                        startActivityForResult(sdintent, request_code);

                    }
                });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == request_code && data != null) {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cur = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                if (cur == null) imgPath.add(null);
                else {
                    int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cur.moveToFirst();
                    imgPath.add(cur.getString(column_index));
                    cur.close();
                }
                bmap.add(BitmapFactory.decodeFile(imgPath.get(image_count)));
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new Edit_promotion_RecyclerView(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            }
        }
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
