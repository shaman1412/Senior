package com.devahoy.sample.Faff.RestaurantProfile;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.devahoy.sample.Faff.utils.BitmapImageManager;

/**
 * Created by InFiNity on 19-Feb-17.
 */

public class async_image extends AsyncTask<String,Void,Bitmap>{
    Context mcontext;
    ImageView view;
    int res_id;
    public async_image(Context context,ImageView view, int res_id){
        mcontext = context;
        this.view = view;
        this.res_id = res_id;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        BitmapImageManager bitmap = new BitmapImageManager();
        return  bitmap.decodeSampledBitmapFromResource(mcontext.getResources(),res_id,300,300);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }
}
