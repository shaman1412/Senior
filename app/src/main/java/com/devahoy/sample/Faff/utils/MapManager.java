package com.devahoy.sample.Faff.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by InFiNity on 21-Jan-17.
 */

import com.devahoy.sample.Faff.model.Marker;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MapManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ahoy_login";

    private SQLiteDatabase mdatabase;

    public MapManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MARKER = String.format("CREATE TABLE %s" +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                Marker.TABLE,
                Marker.column.ID,
                Marker.column.TITLE,
                Marker.column.SNIPPET,
                Marker.column.POSITION
        );

            db.execSQL(CREATE_TABLE_MARKER);
            Log.i(TAG, CREATE_TABLE_MARKER);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_TABLE_MARKER = String.format("DROP TABLE IF EXISTS" + 1);

        db.execSQL(DROP_TABLE_MARKER);
        onCreate(mdatabase);
    }
    public long addMarker(Marker marker ){
        mdatabase = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(Marker.column.TITLE, marker.getMarkerTitle());
        v.put(Marker.column.SNIPPET, marker.getMarkerSnippet());
        v.put(Marker.column.POSITION, marker.getMarkerPostion());

        long result  = mdatabase.insert(Marker.TABLE, null, v);
        mdatabase.close();
        return  result;
    }
    public ArrayList<Marker> getMarker(){

        ArrayList<Marker> obMarker = new ArrayList<Marker>();

        mdatabase = this.getReadableDatabase();
        Cursor cursor = mdatabase.query(Marker.TABLE,
                new String[]{Marker.column.TITLE,Marker.column.SNIPPET,Marker.column.POSITION},
                null,
                null,
                null,
                null,
                null);

        if(cursor != null){
            while(cursor.moveToNext()){
                Marker currentmarker = new Marker();
                currentmarker.setMarkerTitle(cursor.getString(0));
                currentmarker.setMarketSnipper(cursor.getString(1));
                currentmarker.setMarketPosition(cursor.getString(2));
                obMarker.add(currentmarker);
            }

            mdatabase.close();
            cursor.close();
            return obMarker;
        }
        else{
            return  null;
        }


    }
}
