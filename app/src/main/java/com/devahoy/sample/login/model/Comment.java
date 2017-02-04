package com.devahoy.sample.login.model;

/**
 * Created by Not_Today on 1/6/2017.
 */

import android.provider.BaseColumns;

public class Comment {

    public static final String TABLE = "comment";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "UserID";
        public static final String Comment = "Comment";
        public static final String Datetime = "DateTime";
    }

    private int id;
    private int userID;
    private String comment;
    private String dateTime;

    public Comment(int userID, String comment, String dateTime){
        this.userID = userID;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public Comment(){

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

    //endregion

}
