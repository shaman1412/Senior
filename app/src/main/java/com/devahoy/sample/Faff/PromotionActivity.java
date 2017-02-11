package com.devahoy.sample.Faff;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.devahoy.sample.Faff.model.Promotion;
import com.devahoy.sample.Faff.utils.DatabaseManager;
import com.devahoy.sample.Faff.utils.PromotionArrayAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.R.id.list;

public class PromotionActivity extends ListActivity {

    private Button addPromotion;
    private EditText Title;
    private Button uploadPicture;
    //private Button cancel;
    private EditText TitlePictureName;
    private EditText StartDate;
    private EditText EndDate;
    private EditText PromotionDetail;
    private EditText Location;
    private Context mContext;

    private static final int request_code = 1;

    public static ArrayList<Bitmap> bmap = new ArrayList<>();
    public static ArrayList<String> imgPath = new ArrayList<>();
    public static int image_count=0;

    DatabaseManager mManager;
    public static PromotionArrayAdapter adapter;

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

        //cancel = (Button) findViewById(R.id.cancelUpload);

        TitlePictureName = (EditText) findViewById(R.id.titlePictureName);
        StartDate = (EditText) findViewById(R.id.startDate);
        EndDate = (EditText) findViewById(R.id.endDate);
        PromotionDetail = (EditText) findViewById(R.id.promotionDetail);
        Location = (EditText) findViewById(R.id.location);

        //cancel.setVisibility(View.GONE);

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //sdintent.setType("image/*");
                startActivityForResult(sdintent, request_code);
            }
        });

//        cancel.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                img.setImageDrawable(null);
//                cancel.setVisibility(View.GONE);
//                image_count--;
//            }
//        });


//        addPromotion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddPromotion();
//            }
//        });
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

                //img.setImageURI(selectedImg);
                bmap.add(BitmapFactory.decodeFile(imgPath.get(image_count)));
                image_count++;

                adapter = new PromotionArrayAdapter(this, bmap, bmap);
                ListView lv = (ListView) findViewById(list);
                lv.setAdapter(adapter);
                lv.setOnTouchListener(new ListView.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });


                //img.setImageBitmap(bitSelectedImg);

                //convert to bynary
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitSelectedImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteImg = stream.toByteArray();
            }

            //cancel.setVisibility(View.VISIBLE);
        }
    }

//    private void AddPromotion() {
//        String title = Title.getText().toString();
//        String titlePictureName = TitlePictureName
//
//        //get img from url
//        //byte[] titlePicture = TitlePicture.get;
//
//        String startDate = StartDate.getText().toString();
//        String endDate = EndDate.getText().toString();
//        String promotionDetail = PromotionDetail.getText().toString();
//        String location = Location.getText().toString();
//
//        Promotion promotion = new Promotion(title, titlePicture, startDate, endDate, promotionDetail,location);
//
//        long rowId = mManager.addPromotion(promotion);
//
//        if (rowId == -1) {
//            String message = "Add promotion failed try again";
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//        } else {
//            String message = "Add promotion successful";
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
}
