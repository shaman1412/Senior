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
import com.Senior.Faff.utils.Helper;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    private List_type list_adapter;
    private boolean first = true;
    private String type_check;

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
//    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data

    public static int image_count = 0;                                    //number of images

    public static PromotionRecyclerViewAdapter adapter;
    private Spinner type;
    private ArrayList<String> type_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_promotion);

        mContext = this;
        type_list = new ArrayList<>();

        type = (Spinner) findViewById(R.id.type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(mContext, R.array.type_food_dropdown, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter_type);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = type.getSelectedItem().toString();
                boolean same = false;

                for (int i = 0; i < type_list.size(); i++) {
                    if (text.equals(type_list.get(i))) {
                        same = true;
                    }
                }
                if (same == false && first == false) {
            /*        type_list.add(text);
                    list_adapter = new List_type(type_list, mContext);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView = (RecyclerView) findViewById(R.id.list_show);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(list_adapter);*/

                }
                if (first == true) {
                    first = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                ArrayList<String> list_get = list_adapter.getlist();
                for (int i = 0; i < list_get.size(); i++) {
                    if (i == 0) {
                        type_check = list_get.get(i);
                    } else {
                        type_check = type_check + list_get.get(i);
                    }
                    if (i != list_get.size() - 1) {
                        type_check = type_check + ",";
                    }
                }

                String title = Title.getText().toString();
                String startDate = StartDate.getText().toString();
                String endDate = EndDate.getText().toString();
                String promotionDetail = PromotionDetail.getText().toString();
                String location = Location.getText().toString();

                String img_path_tmp = new Gson().toJson(imgPath);

                Promotion promotion = new Promotion(title, img_path_tmp, type_check, startDate, endDate, promotionDetail, location);
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
                paras.put(Promotion.Column.Type, params[0].getType());
                paras.put(Promotion.Column.StartDate, params[0].getStartDate());
                paras.put(Promotion.Column.EndDate, params[0].getEndDate());
                paras.put(Promotion.Column.PromotionDetail, params[0].getPromotionDetail());
                paras.put(Promotion.Column.Location, params[0].getGoogleMapLink());

                String img_path_tmp = params[0].getPromotionpictureurl();
                AddPromotion.this.imgPath = new Gson().fromJson(img_path_tmp, ArrayList.class);

                URL url = new URL("https://faff-1489402013619.appspot.com/promotion_list/new_promotion");
                //URL url = new URL("http://localhost:8080/promotion_list/new_promotion");

                Log.i(TAG, "  background size : " + AddPromotion.this.imgPath.size());
                result = new Helper().multipartRequest(url.toString(),paras, AddPromotion.this.imgPath, "image", "image/jpeg");
                Log.i(TAG, "result for multipartRequest(url.toString(), paras, imgPath.get(0), \"image\", \"image/jpeg\");   :   " + result);

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

}
