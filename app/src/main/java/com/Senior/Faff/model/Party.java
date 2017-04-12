package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class Party {
    public static final String TABLE = "party";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Name = "Name";
        public static final String Description = "Description";
        public static final String Appointment = "Appointment";
        public static final String RoomID = "Roomid";
    }

    private int id;
    private String name;
    private String description;
    private int people;
    private String roomID;
    private String location;
    //private String rule;
    private Map<String, String> rule = new HashMap<>();

    public Party(String roomID,String name,  String decription, int people, Map<String, String> rule,String location) {
        this.name = name;
        this.roomID = roomID;
        this.description = decription;
        this.people = people;
        this.rule = rule;
        this.location  = location;
    }

    public Party(){

    }

    //region getter setter
    public String getLocation(){ return location; }

    public void setLocation(String location){ this.location = location; }

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

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String appoinment) {
        this.description = description;
    }

    public int getPeople(){ return people; }

    public void setPeople(int people){ this.people = people;}

    public Map<String, String> getRule(){ return rule; }

    public void setRule(Map<String, String> rule){ this.rule = rule; }



    //endregion
}
