package com.Senior.Faff;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Senior.Faff.model.Promotion;
import com.Senior.Faff.model.PromotionPicture;
import com.Senior.Faff.utils.DatabaseManager;
import com.devahoy.sample.Faff.utils.PromotionRecyclerViewAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PromotionActivity extends AppCompatActivity {

public class PromotionActivity extends ListActivity {

    public static final String TAG = PromotionActivity.class.getSimpleName();

    private Button addPromotion;
    private EditText Title;
    private Button uploadPicture;
    private EditText TitlePictureName;
    private EditText StartDate;
    private EditText EndDate;
    private EditText PromotionDetail;
    private EditText Location;
    private Context mContext;

    private static final int request_code = 1;                          //request code for OnClick result

    public static ArrayList<Bitmap> bmap = new ArrayList<>();           //keep bitmap data
    public static ArrayList<String> imgPath = new ArrayList<>();        //keep uri
    public static ArrayList<byte[]> imgByte = new ArrayList<>();        //keep byte data

    public static int image_count=0;                                    //number of images

    DatabaseManager mManager;
    public static PromotionRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_promotion);


        mManager = new DatabaseManager(this);

        mContext = this;

        addPromotion = (Button) findViewById(R.id.addPromotion);
        Title = (EditText) findViewById(R.id.title);
        uploadPicture = (Button) findViewById(R.id.titlePicture);
        TitlePictureName = (EditText) findViewById(R.id.titlePictureName);
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
                AddPromotion();
                bmap.clear();
                imgPath.clear();
                imgByte.clear();
                image_count = 0;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode == request_code && data != null)
            {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cur = getContentResolver().query(selectedImg,filePathColumn,null,null,null);
                if(cur==null) imgPath.add(null);
                else
                {
                    int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cur.moveToFirst();
                    imgPath.add(cur.getString(column_index));
                    cur.close();
                }
                bmap.add(BitmapFactory.decodeFile(imgPath.get(image_count)));
                //convert to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.get(image_count).compress(Bitmap.CompressFormat.PNG, 100, stream);
                imgByte.add(stream.toByteArray());
                image_count++;

                adapter = new PromotionRecyclerViewAdapter(this, bmap, bmap);
                RecyclerView lv = (RecyclerView) findViewById(R.id.rlist1);
                lv.setNestedScrollingEnabled(false);
                lv.setAdapter(adapter);


//                lv.setOnTouchListener(new ListView.OnTouchListener(){
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int action = event.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                // Disallow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(true);
//                                break;
//
//                            case MotionEvent.ACTION_UP:
//                                // Allow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                break;
//                        }
//                        // Handle ListView touch events.
//                        v.onTouchEvent(event);
//                        return true;
//                    }
//                });

                //img.setImageBitmap(bitSelectedImg);
            }
        }
    }

    private void AddPromotion() {
        String title = Title.getText().toString();
        String startDate = StartDate.getText().toString();
        String endDate = EndDate.getText().toString();
        String promotionDetail = PromotionDetail.getText().toString();
        String location = Location.getText().toString();

        Promotion promotion = new Promotion(title, startDate, endDate, promotionDetail,location);
        int proID = (int)mManager.getCurrentPromotionID()+1;
        for (byte[] b:imgByte) {
            PromotionPicture pro_pic = new PromotionPicture(proID,b);
            long rowID = mManager.addPromotionPicture(pro_pic);
            if(rowID == -1)
            {
                Log.i(TAG,"add pro_pic " + proID + " fail");
            }
            else
            {
                Log.i(TAG,"add pro_pic " + proID + " success");
            }
        }

        long rowId = mManager.addPromotion(promotion);

        if (rowId == -1) {
            String message = "Add promotion failed try again";
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            String message = "Add promotion successful";
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
