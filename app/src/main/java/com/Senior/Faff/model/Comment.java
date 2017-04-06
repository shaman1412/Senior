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
    private int userID;
    private String comment;
    private String dateTime;
    private String name;

    public Comment(int userID, String name, String comment, String dateTime){
        this.userID = userID;
        this.name = name;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public Comment(){

    }

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
}
