package com.devahoy.sample.Faff.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devahoy.sample.Faff.model.Marker;
import com.devahoy.sample.Faff.model.Promotion;
import com.devahoy.sample.Faff.model.UserAuthen;


public class UserManager extends SQLiteOpenHelper implements DatabaseManagerHelper {
    public static final String TAG = UserManager.class.getSimpleName();
    private SQLiteDatabase mDatabase;

    public UserManager(Context context) {
        super(context, DatabaseManager.DATABASE_NAME, null, DatabaseManager.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)" ,
                UserAuthen.TABLE,
                UserAuthen.Column.ID,
                UserAuthen.Column.USERNAME,
                UserAuthen.Column.PASSWORD
        );
        String CREATE_TABLE_MARKER = String.format("CREATE TABLE %s" +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                Marker.TABLE,
                Marker.column.ID,
                Marker.column.TITLE,
                Marker.column.SNIPPET,
                Marker.column.POSITION
        );
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
        db.execSQL(CREATE_TABLE_MARKER);
        db.execSQL(CREATE_TABLE_USER);

        Log.i(TAG, CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER = "DROP TABLE IF EXISTS " + DatabaseManager.DATABASE_VERSION;

        db.execSQL(DROP_USER);

        Log.i(TAG, DROP_USER);
        onCreate(mDatabase);
    }

    @Override
    public long registerUser(UserAuthen user) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, user.getUsername());
        values.put(UserAuthen.Column.PASSWORD, user.getPassword());

        long result = mDatabase.insert(UserAuthen.TABLE, null, values);
        mDatabase.close();

        return result;
    }

    @Override
    public UserAuthen checkUserLogin(UserAuthen user) {

        mDatabase = this.getReadableDatabase();

        Cursor cursor = mDatabase.query(UserAuthen.TABLE,
                null,
                UserAuthen.Column.USERNAME + " = ? AND " +
                        UserAuthen.Column.PASSWORD + " = ?",
                new String[]{user.getUsername(), user.getPassword()},
                null,
                null,
                null);

        UserAuthen currentUser = new UserAuthen();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                currentUser.setId((int) cursor.getLong(0));
                currentUser.setUsername(cursor.getString(1));
                currentUser.setPassword(cursor.getString(2));
                mDatabase.close();
                return currentUser;
            }
        }

        return null;
    }

    @Override
    public int changePassword(UserAuthen user) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, user.getUsername());
        values.put(UserAuthen.Column.PASSWORD, user.getPassword());

        int row = mDatabase.update(UserAuthen.TABLE,
                values,
                UserAuthen.Column.ID + " = ?",
                new String[] {String.valueOf(user.getId())});

        mDatabase.close();
        return row;
    }
}