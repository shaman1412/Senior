package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Restaurant {

    public static final String TABLE = "restaurant";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String RestaurantName = "RestaurantName";
        public static final String RestaurantPicture = "RestaurantPicture";
        public static final String Location = "Location";
        public static final String RestaurantDetail = "RestaurantDetail";
        public static final String Score = "Score";
        public static final String UserID = "UserID";
        public static final String TypeID = "TypeID";
    }

    private int id;
    private String restaurantName;
    private String restaurantPicture;
    private String location;
    private String restaurantDetail;
    private int score;
    private int userID;
    private int typeID;

    // Constructor
    public Restaurant(String restaurantName, String restaurantPicture, String location, String restaurantDetail, int score, int userID, int typeID) {
        this.restaurantName = restaurantName;
        this.restaurantPicture = restaurantPicture;
        this.location = location;
        this.restaurantDetail = restaurantDetail;
        this.score = score;
        this.userID = userID;
        this.typeID = typeID;
    }

    public  Restaurant(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPicture() {
        return restaurantPicture;
    }

    public void setRestaurantPicture(String restaurantPicture) {
        this.restaurantPicture = restaurantPicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRestaurantDetail() {
        return restaurantDetail;
    }

    public void setRestaurantDetail(String restaurantDetail) {
        this.restaurantDetail = restaurantDetail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    //endregion
}
