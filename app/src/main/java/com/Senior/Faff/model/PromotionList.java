package com.Senior.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class PromotionList {

    public static final String TABLE = "promotion_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String PromotionID = "PromotionID";
        public static final String UserID = "UserID";
    }

    private int id;
    private int promotionID;
    private int userID;

    public PromotionList(int promotionID, int userID){
        this.promotionID = promotionID;
        this.userID = userID;
    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
