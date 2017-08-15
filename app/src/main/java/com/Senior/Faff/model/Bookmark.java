package com.Senior.Faff.Model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Bookmark {

    public static final String TABLE = "bookmark";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String FoodID = "FoodID";
        public static final String RestaurantID = "RestaurantID";
    }

    private int id;
    private int foodID;
    private int restaurantID;

    public Bookmark(int foodID, int restaurantID){
        this.foodID = foodID;
        this.restaurantID = restaurantID;
    }

    public Bookmark(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    //endregion
}
