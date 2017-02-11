package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 12/15/2016.
 */

import android.provider.BaseColumns;

public class Promotion {

    public static final String TABLE = "promotion";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Title = "Title";
        public static final String TitlePicture = "TitlePicture";
        public static final String StartDate = "StartDate";
        public static final String EndDate = "EndDate";
        public static final String PromotionDetail = "PromotionDetail";
        public static final String Location = "Location";
    }

    private int id;
    private String title;
    private int[] picID;
    private String startDate;
    private String endDate;
    private String promotionDetail;
    private String googleMapLink;

    // Constructor
    public Promotion(String title, int[] picID, String startDate, String endDate, String promotionDetail, String googleMapLink) {
        this.title = title;
        for (int i=0;i<picID.length;i++){
            this.picID[i] = picID[i];
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionDetail = promotionDetail;
        this.googleMapLink = googleMapLink;
    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getPicID() {
        return picID;
    }

    public void setPicID(int[] picID) {
        this.picID = picID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(String promotionDetail) {
        this.promotionDetail = promotionDetail;
    }

    public String getGoogleMapLink() {
        return googleMapLink;
    }

    public void setGoogleMapLink(String googleMapLink) {
        this.googleMapLink = googleMapLink;
    }

    //endregion
}
