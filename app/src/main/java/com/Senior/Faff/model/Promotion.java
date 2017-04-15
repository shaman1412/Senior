package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/15/2016.
 */

import android.provider.BaseColumns;

import java.util.ArrayList;

public class Promotion {

    public static final String TABLE = "promotion";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Title = "promotionname";
        public static final String Picture = "promotionpictureurl";
        public static final String Type = "promotiontype";
        public static final String StartDate = "promotionstartdate";
        public static final String EndDate = "promotionenddate";
        public static final String PromotionDetail = "promotiondetail";
        public static final String Location = "promotionlocation";
    }

    private int promotionid;
    private String promotionname;
    private String promotionstartdate;
    private String promotionenddate;
    private String promotiondetail;
    private String promotionlocation;
    private String promotiontype;
    private String promotionpictureurl;

    // Constructor

    public Promotion(String promotionname, String promotionpictureurl, String promotiontype, String promotionstartdate, String promotionenddate, String promotiondetail, String promotionlocation) {
        this.promotionname = promotionname;
        this.promotionstartdate = promotionstartdate;
        this.promotionenddate = promotionenddate;
        this.promotiondetail = promotiondetail;
        this.promotionlocation = promotionlocation;
        this.promotiontype = promotiontype;
        this.promotionpictureurl = promotionpictureurl;
    }

    //region getter setter
    public int getId() {
        return promotionid;
    }

    public void setId(int promotionid) {
        this.promotionid = promotionid;
    }

    public String getTitle() {
        return promotionname;
    }

    public void setTitle(String promotionname) {
        this.promotionname = promotionname;
    }

    public String getStartDate() {
        return promotionstartdate;
    }

    public void setStartDate(String promotionstartdate) {
        this.promotionstartdate = promotionstartdate;
    }

    public String getEndDate() {
        return promotionenddate;
    }

    public void setEndDate(String promotionenddate) {
        this.promotionenddate = promotionenddate;
    }

    public String getPromotionDetail() {
        return promotiondetail;
    }

    public void setPromotionDetail(String promotiondetail) {
        this.promotiondetail = promotiondetail;
    }

    public String getGoogleMapLink() {
        return promotionlocation;
    }

    public void setGoogleMapLink(String promotionlocation) {
        this.promotionlocation = promotionlocation;
    }

    public String getType() {
        return promotiontype;
    }

    public void setType(String promotiontype) {
        this.promotiontype = promotiontype;
    }

    public String getPromotionpictureurl() {
        return promotionpictureurl;
    }

    public void setPromotionpictureurl(String promotionpictureurl) {
        this.promotionpictureurl = promotionpictureurl;
    }

    //endregion

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionid=" + promotionid +
                ", promotionname='" + promotionname + '\'' +
                ", promotionstartdate='" + promotionstartdate + '\'' +
                ", promotionenddate='" + promotionenddate + '\'' +
                ", promotiondetail='" + promotiondetail + '\'' +
                ", promotionlocation='" + promotionlocation + '\'' +
                ", promotiontype='" + promotiontype + '\'' +
                ", promotionpictureurl='" + promotionpictureurl + '\'' +
                '}';
    }
}
