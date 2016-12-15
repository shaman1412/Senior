package com.devahoy.sample.login.model;

/**
 * Created by Not_Today on 12/15/2016.
 */

import android.provider.BaseColumns;

public class Promotion {

    public static final String TABLE = "promotion";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Title = "title";
        public static final String TitlePicture = "title picture link";
        public static final String StartDate = "start date";
        public static final String EndDate = "end date";
        public static final String PromotionDetail = "detail";
        public static final String Location = "google map link";
    }

    private int id;
    private String title;
    private String titlePictureLink;
    private String startDate;
    private String endDate;
    private String promotionDetail;
    private String googleMapLink;

    // Constructor
    public Promotion(String title, String titlePictureLink, String startDate, String endDate, String promotionDetail, String googleMapLink) {
        this.title = title;
        this.titlePictureLink = titlePictureLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionDetail = promotionDetail;
        this.googleMapLink = googleMapLink;
    }

    public Promotion() {

    }

    //region getter setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePictureLink() {
        return titlePictureLink;
    }

    public void setTitlePictureLink(String titlePictureLink) {
        this.titlePictureLink = titlePictureLink;
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
