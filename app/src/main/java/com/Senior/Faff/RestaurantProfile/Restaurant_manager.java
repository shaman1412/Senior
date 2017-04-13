package com.Senior.Faff.RestaurantProfile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.utils.DatabaseManagerHelper;

import java.util.ArrayList;

public class Restaurant_manager extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;
    private String type_result;
    public Restaurant_manager(Context context) {
        super(context, DatabaseManagerHelper.DATABASE_NAME, null, DatabaseManagerHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
/*    public long  AddRestaurant(Restaurant restaurant){
        mDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Restaurant.Column.RestaurantName,restaurant.getRestaurantName());
        values.put(Restaurant.Column.RestaurantPicture,restaurant.getRestaurantPicture());
        values.put(Restaurant.Column.RestaurantDetail,restaurant.getRestaurantDetail());
        values.put(Restaurant.Column.TypeID,restaurant.getTypeID());
        values.put(Restaurant.Column.Location,restaurant.getLocation());
        values.put(Restaurant.Column.UserID,restaurant.getUserID());
        values.put(Restaurant.Column.Score,restaurant.getScore());
        long result  = mDatabase.insert(Restaurant.TABLE,null,values);

        mDatabase.close();
    return result;
    }*/
/*    public Restaurant showRestaurant(String user_id,String res_id){
        mDatabase = getReadableDatabase();

        Cursor cursor = mDatabase.query(Restaurant.TABLE,
                null,
                Restaurant.Column.UserID + " = ? AND " + Restaurant.Column.ID + " = ?", new String[]{user_id,res_id},
                null,
                null,
                null
        );
        Restaurant model = new Restaurant();
        if(cursor!=null){
            cursor.moveToFirst();
            model.setId((int)cursor.getLong(0));
            model.setRestaurantName(cursor.getString(1));
            model.setRestaurantPicture(cursor.getString(2));
            model.setLocation(cursor.getString(3));
            model.setRestaurantDetail(cursor.getString(4));
            model.setScore((int)cursor.getLong(5));
            model.setUserID(cursor.getString(6));
            model.setTypeID((int)cursor.getLong(7));
        }
        mDatabase.close();

        return model;
    }*/
  /*  public ArrayList<Restaurant> showAllRestaurant(){
        mDatabase = getReadableDatabase();

        Cursor cursor = mDatabase.query(Restaurant.TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<Restaurant> list = new ArrayList<>();

        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    Restaurant model = new Restaurant();
                    model.setId((int) cursor.getLong(0));
                    model.setRestaurantName(cursor.getString(1));
                    model.setRestaurantPicture(cursor.getString(2));
                    model.setLocation(cursor.getString(3));
                    model.setRestaurantDetail(cursor.getString(4));
                    model.setScore((int) cursor.getLong(5));
                    model.setUserID( cursor.getString(6));
                    model.setTypeID((int) cursor.getLong(7));
                    list.add(model);
                }while(cursor.moveToNext());
            }
        }
        mDatabase.close();

        return list;
    }*/

}
