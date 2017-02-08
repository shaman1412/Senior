package com.devahoy.sample.Faff.utils;

/**
 * Created by Not_Today on 12/15/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devahoy.sample.Faff.model.Promotion;

public class PromotionManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ahoy_login";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = PromotionManager.class.getSimpleName();
    private SQLiteDatabase mDatabase;

    public PromotionManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_PROMOTION = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)" ,
                Promotion.TABLE,
                Promotion.Column.ID,
                Promotion.Column.Title,
                Promotion.Column.TitlePicture,
                Promotion.Column.StartDate,
                Promotion.Column.EndDate,
                Promotion.Column.PromotionDetail,
                Promotion.Column.Location
        );

            db.execSQL(CREATE_TABLE_PROMOTION);
            Log.i(TAG, CREATE_TABLE_PROMOTION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_PROMOTION = "DROP TABLE IF EXISTS " + DATABASE_VERSION;

        db.execSQL(DROP_PROMOTION);

        Log.i(TAG, DROP_PROMOTION);
        onCreate(mDatabase);
    }

    public long addPromotion(Promotion promotion) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Promotion.Column.Title, promotion.getTitle());
        values.put(Promotion.Column.TitlePicture, promotion.getTitlePictureLink());
        values.put(Promotion.Column.StartDate, promotion.getStartDate());
        values.put(Promotion.Column.EndDate, promotion.getEndDate());
        values.put(Promotion.Column.PromotionDetail, promotion.getPromotionDetail());
        values.put(Promotion.Column.Location, promotion.getGoogleMapLink());

        long result = mDatabase.insert(Promotion.TABLE, null, values);
        mDatabase.close();

        return result;
    }
}
