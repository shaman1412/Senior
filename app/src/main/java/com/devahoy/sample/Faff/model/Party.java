package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Party {
    public static final String TABLE = "party";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Name = "Name";
        public static final String RoomID = "RoomID";
        public static final String Appointment = "Appointment";
    }

    private int id;
    private String name;
    private int roomID;
    private String appoinment;

    public Party(String name, int roomID, String appoinment) {
        this.name = name;
        this.roomID = roomID;
        this.appoinment = appoinment;
    }

    public Party(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getAppoinment() {
        return appoinment;
    }

    public void setAppoinment(String appoinment) {
        this.appoinment = appoinment;
    }

    //endregion
}
