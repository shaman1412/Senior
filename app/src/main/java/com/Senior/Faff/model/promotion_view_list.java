package com.Senior.Faff.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Not_Today on 2/13/2017.
 */
public class promotion_view_list {
    String id;
    String title;
    ArrayList<Bitmap> picture;
    String type;
    String startDate;
    String endDate;
    String promotionDetail;
    String location;

    public promotion_view_list(String id, String title, ArrayList<Bitmap> picture, String type, String startDate, String endDate, String promotionDetail, String location) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionDetail = promotionDetail;
        this.location = location;
    }

    //region getter
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    //endregion getter

    @Override
    public String toString() {
        return "promotion_view_list{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", picture=" + picture +
                ", type='" + type + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", promotionDetail='" + promotionDetail + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
