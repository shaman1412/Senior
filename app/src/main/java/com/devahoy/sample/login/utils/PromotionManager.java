package com.devahoy.sample.login.utils;

/**
 * Created by Not_Today on 12/15/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.devahoy.sample.login.model.Promotion;

public class PromotionManager {

    public static final String DATABASE_NAME = "promotion";
    public static final String DATABASE_TABLE = "promotion";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = PromotionManager.class.getSimpleName();

    static final String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null);" ,
            DATABASE_TABLE,
            Promotion.Column.ID,
            Promotion.Column.Title,
            Promotion.Column.TitlePicture,
            Promotion.Column.StartDate,
            Promotion.Column.EndDate,
            Promotion.Column.PromotionDetail,
            Promotion.Column.Location
    );

    private final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase mDatabase;

    public PromotionManager(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.i(TAG, DATABASE_CREATE);
            try {
                db.execSQL(DATABASE_CREATE);
                Log.i(TAG, "Create Success");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(TAG, "Create failed");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }


    public long addPromotion(Promotion promotion) {

        mDatabase = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Promotion.Column.Title, promotion.getTitle());
        values.put(Promotion.Column.TitlePicture, promotion.getTitlePictureLink());
        values.put(Promotion.Column.StartDate, promotion.getStartDate());
        values.put(Promotion.Column.EndDate, promotion.getEndDate());
        values.put(Promotion.Column.PromotionDetail, promotion.getPromotionDetail());
        values.put(Promotion.Column.Location, promotion.getGoogleMapLink());

        long result = mDatabase.insert(DATABASE_TABLE, null, values);
        mDatabase.close();

        return result;
    }
}
