package com.devahoy.sample.Faff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devahoy.sample.Faff.utils.BitmapImageManager;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imager);
        ImageViewTouch image = (ImageViewTouch)findViewById(R.id.image);

        BitmapImageManager bitmap = new BitmapImageManager();

     /*   Bitmap bitmap = DecodeUtils.decode(this
                ,icon, 2048, 2048);*/
       image.setImageBitmap(
                bitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.woman, 100, 100));
    }

}
