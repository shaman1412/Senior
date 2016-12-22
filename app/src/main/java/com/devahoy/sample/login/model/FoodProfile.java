package com.devahoy.sample.login.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class FoodProfile {
    public static final String TABLE = "food_profile";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String restaurantID = "RestaurantID";
        public static final String Name = "Name";
        public static final String FoodPicture = "FoodPicture";
        public static final String FoodDetail = "FoodDetail";
        public static final String TypeID = "TypeID";
    }

    private int id;
    private int restaurantID;
    private String name;
    private String foodPicture;
    private String foodDetail;
    private int typeID;

    public FoodProfile(int restaurantID, String name, String foodPicture, String foodDetail, int typeID){
        this.restaurantID = restaurantID;
        this.name = name;
        this.foodPicture = foodPicture;
        this.foodDetail = foodDetail;
        this.typeID = typeID;
    }

    public FoodProfile(){

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodPicture() {
        return foodPicture;
    }

    public void setFoodPicture(String foodPicture) {
        this.foodPicture = foodPicture;
    }

    public String getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(String foodDetail) {
        this.foodDetail = foodDetail;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    //endregion
}
