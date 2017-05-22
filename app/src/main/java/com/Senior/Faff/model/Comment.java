package com.Senior.Faff.model;

/**
 * Created by Not_Today on 1/6/2017.
 */

import android.provider.BaseColumns;

public class Comment {

    public static final String TABLE = "comment";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "id";
        public static final String UserName = "name";
        public static final String Comment = "comment";
        public static final String Datetime = "datetime";
    }

    private int id;
    private String userID;
    private String comment;
    private String dateTime;
    private String name;
    private String picture;

    public Comment(String userID, String name, String comment, String dateTime, String picture){
        this.userID = userID;
        this.name = name;
        this.comment = comment;
        this.dateTime = dateTime;
        this.picture = picture;
    }

    public Comment(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userID='" + userID + '\'' +
                ", comment='" + comment + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
