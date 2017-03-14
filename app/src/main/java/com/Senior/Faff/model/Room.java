package com.Senior.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Room {
    public static final String TABLE = "room";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String RoomID = "RoomID";
        public static final String Message = "Message";
    }

    private int id;
    private int roomID;
    private String message;

    public Room(int roomID, String message){
        this.roomID = roomID;
        this.message = message;
    }

    public Room(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //endregion
}
