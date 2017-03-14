package com.Senior.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class RoomList {
    public static final String TABLE = "room_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "UserID";
    }

    private int id;
    private int userID;

    public RoomList(int userID){
        this.userID = userID;
    }

    public RoomList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
