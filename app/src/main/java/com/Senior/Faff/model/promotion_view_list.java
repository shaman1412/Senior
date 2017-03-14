package com.Senior.sample.Faff.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Not_Today on 2/13/2017.
 */
public class promotion_view_list{
    String id;
    String title;
    ArrayList<Bitmap> picture;
    String startDate;
    String endDate;
    String promotionDetail;
    String location;

    public promotion_view_list(String id, String title, ArrayList<Bitmap> picture, String startDate, String endDate, String promotionDetail, String location) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionDetail = promotionDetail;
        this.location = location;
    }

    //region getter setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Bitmap> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<Bitmap> picture) {
        this.picture = picture;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //endregion
}
