package com.Senior.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class ReviewList {
    public static final String TABLE = "review_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String ReviewID = "ReviewID";
        public static final String UserID = "UserID";
    }

    private int id;
    private int reviewID;
    private int userID;

    public ReviewList(int reviewID, int userID){
        this.reviewID = reviewID;
        this.userID = userID;
    }

    public ReviewList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
