package com.devahoy.sample.login.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Review {
    public static final String TABLE = "review";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String RestaurantID = "RestaurantID";
        public static final String FoodID = "FoodID";
        public static final String Date = "Date";
        public static final String Detail = "Detail";
        public static final String Score = "Score";
    }

    private int id;
    private int restaurantID;
    private int foodID;
    private String date;
    private String detail;
    private int score;

    public Review(int restaurantID, int foodID, String date, String detail, int score){
        this.restaurantID = restaurantID;
        this.foodID = foodID;
        this.detail = detail;
        this.date = date;
        this.score = score;
    }

    public Review(){

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

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //endregion
}
