package com.Senior.Faff.model;

import android.provider.BaseColumns;

public class UserAuthen {

    public static final String TABLE = "user";

    public class Column {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String USERID = "userid";

    }

    private String userid;
    private String username;
    private String password;

    // Constructor
    public UserAuthen(String username, String password, String userid) {
        this.username = username;
        this.password = password;
        this.userid = userid;
    }

    public UserAuthen() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
