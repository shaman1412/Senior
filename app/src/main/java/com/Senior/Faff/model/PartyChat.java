package com.Senior.Faff.model;

import android.provider.BaseColumns;

/**
 * Created by Not_Today on 5/1/2017.
 */

public class PartyChat {

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String user_name = "user_name";
        public static final String image_path = "image_path";
        public static final String message = "message";
        public static final String date_time = "date_time";
    }

    private String username;
    private String image_path;
    private String message;
    private String date_time;

    public PartyChat(String username, String image_path, String message, String date_time) {
        this.username = username;
        this.image_path = image_path;
        this.message = message;
        this.date_time = date_time;
    }

    public PartyChat(){

    }

    //region getter setter

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }


    //endregion

}
