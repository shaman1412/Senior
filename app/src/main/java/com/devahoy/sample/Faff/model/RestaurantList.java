package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class RestaurantList {
    public static final String TABLE = "restaurant_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String RestaurantID = "RestaurantID";
        public static final String UserID = "UserID";
    }

    private int id;
    private int restaurantID;
    private int userID;

    public RestaurantList(int restaurantID, int userID)
    {
        this.userID = userID;
        this.restaurantID = restaurantID;
    }

    public RestaurantList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
