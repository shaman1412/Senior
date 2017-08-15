package com.Senior.Faff.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.google.gson.Gson;

import java.util.List;

public class ProfileManager extends SQLiteOpenHelper {
    private SQLiteDatabase mdatabase;

    public ProfileManager(Context context) {
        super(context, DatabaseManagerHelper.DATABASE_NAME, null, DatabaseManagerHelper.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DROP_USER = "DROP TABLE IF EXISTS " + DatabaseManagerHelper.DATABASE_VERSION;
        db.execSQL(DROP_USER);

    }

    public String getUserProfile(String id) {

        mdatabase = this.getReadableDatabase();
        Cursor cursor = mdatabase.query(UserProfile.TABLE,
                null,
                UserProfile.Column.ID + "=?", new String[]{id},
                null,
                null,
                null
        );
        UserProfile profile = new UserProfile();
        if(cursor!=null){
        cursor.moveToFirst();
        profile.setId(cursor.getString(0));
        profile.setName(cursor.getString(1));
        profile.setAddress(cursor.getString(2));
        profile.setEmail(cursor.getString(3));
        //profile.setTelephone((int) (cursor.getLong(4)));
        //profile.setDateOfBirth(cursor.getString(5));
      //  profile.setGender(cursor.getString(6));
        profile.setAge((int) cursor.getLong(7));
        profile.setPicture(cursor.getString(8));
        }
        mdatabase.close();
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        return json;
    }

    public int getID(String Username) {
        mdatabase = this.getReadableDatabase();
        Cursor cursor = mdatabase.query(UserAuthen.TABLE,
                new String[]{UserAuthen.Column.USERID},
                UserAuthen.Column.USERNAME + "=?", new String[]{Username},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        int id = ((int) cursor.getLong(0));
        return id;
    }

    public String getUserName(String id){
        mdatabase = this.getReadableDatabase();
        Cursor cursor = mdatabase.query(UserAuthen.TABLE,
                new String[]{UserAuthen.Column.USERNAME},
                UserAuthen.Column.USERID + "=?", new String[]{id},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        String userName = ((String)cursor.getString(0));
        return userName;
    }

    public long insetUserprofile(UserProfile userProfile){
            mdatabase = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserProfile.Column.Name,userProfile.getName());
        values.put(UserProfile.Column.Address,userProfile.getAddress());
        values.put(UserProfile.Column.Email,userProfile.getEmail());
        values.put(UserProfile.Column.Telephone,userProfile.getTelephone());
       // values.put(UserProfile.Column.DateOfBirth,userProfile.getDateOfBirth());
        values.put(UserProfile.Column.Gender,userProfile.getGender());
        values.put(UserProfile.Column.Age,userProfile.getAge());
        values.put(UserProfile.Column.Picture,userProfile.getPicture());
        long result = mdatabase.insert(UserProfile.TABLE,null,values);
        return result;
    }

}
class DataWrapper {
    public Data data;

    public static DataWrapper fromJson(String s) {
        return new Gson().fromJson(s, DataWrapper.class);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
class Data {
    public List<Translation> translations;
}
class Translation {
    public String translatedText;
}
