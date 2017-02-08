package com.devahoy.sample.Faff.model;

import android.provider.BaseColumns;

public class UserAuthen {

    public static final String TABLE = "user";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    private int id;
    private String username;
    private String password;

    // Constructor
    public UserAuthen(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAuthen() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
