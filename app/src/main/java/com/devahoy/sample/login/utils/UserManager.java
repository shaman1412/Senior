package com.devahoy.sample.login.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devahoy.sample.login.model.UserAuthen;

public class UserManager extends SQLiteOpenHelper implements UserManagerHelper {
    public static final String TAG = UserManager.class.getSimpleName();
    private SQLiteDatabase mDatabase;

    public UserManager(Context context) {
        super(context, UserManagerHelper.DATABASE_NAME, null, UserManagerHelper.DATABASE_VERSION);
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

        db.execSQL(CREATE_TABLE_USER);

        Log.i(TAG, CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER = "DROP TABLE IF EXISTS " + UserManagerHelper.DATABASE_VERSION;

        db.execSQL(DROP_USER);

        Log.i(TAG, DROP_USER);
        onCreate(mDatabase);
    }

    @Override
    public long registerUser(UserAuthen userAuthen) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, userAuthen.getUsername());
        values.put(UserAuthen.Column.PASSWORD, userAuthen.getPassword());

        long result = mDatabase.insert(UserAuthen.TABLE, null, values);
        mDatabase.close();

        return result;
    }

    @Override
    public UserAuthen checkUserLogin(UserAuthen userAuthen) {

        mDatabase = this.getReadableDatabase();

        Cursor cursor = mDatabase.query(UserAuthen.TABLE,
                null,
                UserAuthen.Column.USERNAME + " = ? AND " +
                        UserAuthen.Column.PASSWORD + " = ?",
                new String[]{userAuthen.getUsername(), userAuthen.getPassword()},
                null,
                null,
                null);

        UserAuthen currentUserAuthen = new UserAuthen();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                currentUserAuthen.setId((int) cursor.getLong(0));
                currentUserAuthen.setUsername(cursor.getString(1));
                currentUserAuthen.setPassword(cursor.getString(2));
                mDatabase.close();
                return currentUserAuthen;
            }
        }

        return null;
    }

    @Override
    public int changePassword(UserAuthen userAuthen) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, userAuthen.getUsername());
        values.put(UserAuthen.Column.PASSWORD, userAuthen.getPassword());

        int row = mDatabase.update(UserAuthen.TABLE,
                values,
                UserAuthen.Column.ID + " = ?",
                new String[] {String.valueOf(userAuthen.getId())});

        mDatabase.close();
        return row;
    }
}