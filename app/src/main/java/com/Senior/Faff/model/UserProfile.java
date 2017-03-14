package com.Senior.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class UserProfile {

    public static final String TABLE = "user_profile";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Name = "Name";
        public static final String Address = "Address";
        public static final String Email = "Email";
        public static final String Telephone = "Telephone";
        public static final String DateOfBirth = "DateOfBirth";
        public static final String Gender = "Gender";
        public static final String Age = "Age";
        public static final String Picture = "Picture";
    }

    private int id;
    private String name;
    private String address;
    private String email;
    private int telephone;
    private String dateOfBirth;
    private String gender;
    private int age;
    private String picture;

    public UserProfile(String name, String address, String email, int telephone, String dateOfBirth, String gender, int age, String picture) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.telephone = telephone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.age = age;
        this.picture = picture;
    }

    public UserProfile(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    //endregion
}
