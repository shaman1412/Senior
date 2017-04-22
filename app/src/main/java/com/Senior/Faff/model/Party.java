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
        public static final String Name = "name";
        public static final String Description = "description";
        public static final String Appointment = "appointment";
        public static final String People = "people";
        public static final String Rule = "rule";
        public static final String Request = "request";
        public static final String Accept = "accept";
        public static final String Location = "location";
        public static final String Address = "address";
        public static final String Telephone = "telephone";
        public static final String RoomID = "roomid";
        public static final String Rule_gender = "gender";
        public static final String Rule_age = "age";
        public static final String Create_by = "userid";
        public static final String Create_by_name = "create_by_name";
        public static final String picture = "picture";

    }

    private int id;
    private String name;
    private String description;
    private int people;
    private String roomid;
    private String location;
    private String appointment;
    private String request;
    private String accept;
    private String address;
    private String telephone;
    private Map<String, String> rule = new HashMap<>();
    private String rule_gender;
    private String rule_age;
    private String picture;
    private String create_by_name;
    private String create_by_id;

    public Party(String roomid, String name,  String description, int people, String address ,String appointment, String telephone) {
        this.name = name;
        this.roomid = roomid;
        this.description = description;
        this.people = people;
        this.address = address;
        this.appointment  = appointment;
        this.telephone = telephone;
    }
    public Party(){

    }

    //region getter setter

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreateid(){ return  create_by_id;}

    public void setCreateid(String create_by_id){
        this.create_by_id = create_by_id;
    }

    public String getCreatename(){ return  create_by_name;}

    public void setCreatename(String create_by_name){
        this.create_by_name = create_by_name;
    }

    public String getRequest(){ return  request;}

    public void setRequest(String request){ this.request = request; }

    public String getAccept(){ return  accept; }

    public void setAccept(String accept){ this.accept = accept; }


    public String getLocation(){ return location; }

    public void setLocation(String location){ this.location = location; }

    public String getAppointment(){ return appointment; }

    public void setAppointment(String appointment){ this.appointment = appointment; }

    public String getTelephone(){ return telephone; }

    public void setTelephone(String telephone){ this.telephone = telephone; }

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
        return roomid;
    }

    public void setRoomID(String roomid) {
        this.roomid = roomid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPeople(){ return people; }

    public void setPeople(int people){ this.people = people;}

    public Map<String, String> getRule(){ return rule; }

    public void setRule(Map<String, String> rule){ this.rule = rule; }

    public void setAddress(String address){ this.address = address; }

    public  String getAddress(){ return  address; }



    //endregion
}
