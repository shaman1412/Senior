package com.Senior.Faff.Model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import java.sql.Timestamp;

public class Restaurant {

    public static final String TABLE = "restaurant";

    public class Column {
        public static final String ResID = "resid";
        public static final String RestaurantName = "name";
        public static final String RestaurantPicture = "picture";
        public static final String TypeFood = "type_food";
        public static final String Description = "description";
        public static final String Period = "period";
        public static final String Address = "address";
        public static final String Location = "location";
        public static final String Telephone = "telephone";
        public static final String CreateTime = "create_time";
        public static final String Score = "Score";
        public static final String UserID = "userid";

    }

    private String resid;
    private String name;
    private String picture;
    private String type_food;
    private String description;
    private String period;
    private String address;
    private String  telephone;
    private String location;
    private Timestamp create_time;
    private int score;
    private String userid;


    // Constructor
    public Restaurant(String name, String address, String description,String period, String telephone, String userid,String type_food) {
        this.name = name;
        this.address= address;
        this.telephone = telephone;
        this.description = description;
        this.userid = userid;
        this.type_food = type_food;
        this.period = period;

    }

    public  Restaurant(){

    }

    //region getter setter

    public String getresId() {
        return resid;
    }

    public void setresId(String resid) {
        this.resid = resid;
    }

    public String getRestaurantName() {
        return name;
    }

    public void setRestaurantName(String restaurantName) {
        this.name = restaurantName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserID() {
        return userid;
    }

    public void setUserID(String userID) {
        this.userid = userid;
    }

    public String getTypefood() {
        return type_food;
    }

    public void setTypefood(String type_food) {
        this.type_food = type_food;
    }

    public  void setCreate_time(Timestamp create_time){ this.create_time = create_time; }

    public  Timestamp getCreate_time(){ return create_time; }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){ return  address;}

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public String getTelephone(){ return  telephone;}

    public void setPeriod(String period){
        this.period = period;
    }

    public String getPeriod(){ return  period;}

    //endregion

}
