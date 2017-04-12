package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class UserProfile {

    public static final String TABLE = "user_profile";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "userid";
        public static final String Name = "name";
        public static final String Address = "address";
        public static final String Email = "email";
        public static final String Telephone = "telephone";
        public static final String Gender = "gender";
        public static final String Favourite_type = "favourite_type";
        public static final String Age = "age";
        public static final String Picture = "picture";
        public static final String Userid = "userid";
    }

    private String id;
    private String name;
    private String address;
    private String email;
    private String telephone;
    private String favourite_type;
    private int gender;
    private int age;
    private String picture;
    private String userid;

    public UserProfile(String userid, String name, String address, String email, String telephone, String favourite_type, int gender, int age, String picture) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.telephone = telephone;
        this.favourite_type = favourite_type;
        this.gender = gender;
        this.age = age;
        this.picture = picture;
        this.userid = userid;
    }

    public UserProfile(){

    }

    //region getter setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFavourite_type() {
        return favourite_type;
    }

    public void setFavourite_type(String favourite_type) {
        this.favourite_type = favourite_type;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserid(String userid){ this.userid = userid ; }

    public String getUserid(){
        return  userid;
    }

    //endregion
}
