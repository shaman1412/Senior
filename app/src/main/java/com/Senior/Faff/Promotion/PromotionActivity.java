package com.Senior.Faff.Promotion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.Main3Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.UserProfile.List_type;
import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.Restaurant_Promotion;
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PromotionActivity extends AppCompatActivity {

    public static final String TAG = PromotionActivity.class.getSimpleName();

    private Button addPromotion;
    private EditText Title;
    private Button uploadPicture;
    private EditText StartDate;
    private EditText EndDate;
    private EditText PromotionDetail;
    private EditText Location;
    private Context mContext;
    private RecyclerView mRecyclerView;
    //    private List_type list_adapter;
    private boolean first = true;
//    private String type_check;

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
//    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data

    public static int image_count = 0;                                    //number of images

    public static PromotionRecyclerViewAdapter adapter;
    private static String resid;
//    private Spinner type;
//    private ArrayList<String> type_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_promotion);

        resid = getIntent().getExtras().getString(Restaurant.Column.ResID);

        mContext = this;
//        type_list = new ArrayList<>();

//        type = (Spinner) findViewById(R.id.type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(mContext, R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
//        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        type.setAdapter(adapter_type);

//        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String text = type.getSelectedItem().toString();
//                boolean same = false;
//
//                for (int i = 0; i < type_list.size(); i++) {
//                    if (text.equals(type_list.get(i))) {
//                        same = true;
//                    }
//                }
//                if (same == false && first == false) {
//                    type_list.add(text);
//                    list_adapter = new List_type(type_list, mContext);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mRecyclerView.setAdapter(list_adapter);
//                }
//                if (first == true) {
//                    first = false;
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        addPromotion = (Button) findViewById(R.id.addPromotion);
        Title = (EditText) findViewById(R.id.title);
        uploadPicture = (Button) findViewById(R.id.titlePicture);
        StartDate = (EditText) findViewById(R.id.startDate);
        EndDate = (EditText) findViewById(R.id.endDate);
        PromotionDetail = (EditText) findViewById(R.id.promotionDetail);
        Location = (EditText) findViewById(R.id.location);

        //Need edition for more folder gallery to select
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //sdintent.setType("image/*");
                startActivityForResult(sdintent, request_code);

            }
        });

        addPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<String> list_get = list_adapter.getlist();
//                for (int i = 0; i < list_get.size(); i++) {
//                    if (i == 0) {
//                        type_check = list_get.get(i);
//                    } else {
//                        type_check = type_check + list_get.get(i);
//                    }
//                    if (i != list_get.size() - 1) {
//                        type_check = type_check + ",";
//                    }
//                }

                String title = Title.getText().toString();
                String startDate = StartDate.getText().toString();
                String endDate = EndDate.getText().toString();
                String promotionDetail = PromotionDetail.getText().toString();
                String location = Location.getText().toString();

                String img_path_tmp = new Gson().toJson(imgPath);

                Promotion promotion = new Promotion(title, img_path_tmp, startDate, endDate, promotionDetail, location);
                PromotionActivity.AddPromotion add_pro = new PromotionActivity.AddPromotion();
                add_pro.execute(promotion);

                bmap.clear();
                imgPath.clear();
//                imgByte.clear();
                image_count = 0;
            }
        });
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

                adapter = new PromotionRecyclerViewAdapter(this, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);
            }
        }
    }

    private class AddPromotion extends AsyncTask<Promotion, String, String> {

        private ArrayList<String> imgPath = new ArrayList<>();
        String result = "";

        @Override
        protected String doInBackground(Promotion... params) {
            try {
                Map<String, String> paras = new HashMap<>();
                paras.put(Promotion.Column.ID, String.valueOf(params[0].getId()));
                paras.put(Promotion.Column.Title, params[0].getTitle());
//                paras.put(Promotion.Column.Type, params[0].getType());
                paras.put(Promotion.Column.StartDate, params[0].getStartDate());
                paras.put(Promotion.Column.EndDate, params[0].getEndDate());
                paras.put(Promotion.Column.PromotionDetail, params[0].getPromotionDetail());
                paras.put(Promotion.Column.Location, params[0].getGoogleMapLink());

                String img_path_tmp = params[0].getPromotionpictureurl();
                AddPromotion.this.imgPath = new Gson().fromJson(img_path_tmp, ArrayList.class);

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/new_promotion");

                Helper hp = new Helper();
                hp.setRequest_method("POST");
                result = hp.multipartRequest(url.toString(), paras, AddPromotion.this.imgPath, "image", "image/jpeg");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {

                getCount gc = new getCount();
                gc.execute("");

                //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class getCount extends AsyncTask<String, String, String> {

        private String result;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/get_count");

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
                    JSONArray item = new JSONArray(result);
                    String n = item.getJSONObject(0).getString("n");

                    getCount.Linking lk = new getCount.Linking();
                    lk.execute(new Restaurant_Promotion(resid, n));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }

        private class Linking extends AsyncTask<Restaurant_Promotion, String, String> {
            private HttpURLConnection connection;
            private int responseCode;

            @Override
            protected String doInBackground(Restaurant_Promotion... params) {
                try {
                    JSONObject paras = new JSONObject();
                    paras.put(Restaurant_Promotion.Column.resid, params[0].getResid());
                    paras.put(Restaurant_Promotion.Column.promotionid, params[0].getPromotionid());

                    URL url = new URL("https://faff-1489402013619.appspot.com/restaurant_promotion/create");
                    //URL url = new URL("http://localhost:8080/promotion_list/new_promotion");

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    bufferedWriter.write(getPostDataString(paras));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    out.close();

                    responseCode = connection.getResponseCode();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (responseCode == 200) {
                    Log.i("TEST:", " res_promo This is success response status from server: " + responseCode);

                    return String.valueOf(responseCode);
                } else {
                    Log.i("Request Status", "Tsis is failure response status from server: " + responseCode);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != "") {
                    finish();
                    //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                }
            }

            public String getPostDataString(JSONObject params) throws Exception {

                StringBuilder result = new StringBuilder();
                boolean first = true;

                Iterator<String> itr = params.keys();

                while (itr.hasNext()) {

                    String key = itr.next();
                    Object value = params.get(key);

                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(key, "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(value.toString(), "UTF-8"));

                }
                return result.toString();
            }
        }
    }


}