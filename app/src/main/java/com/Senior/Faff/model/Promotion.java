package com.Senior.Faff.Model;

/**
 * Created by Not_Today on 12/15/2016.
 */

public class Promotion {

    public static final String TABLE = "promotion";

    public class Column {
        public static final String ID = "promotionid";
        public static final String Title = "promotionname";
        public static final String Picture = "promotionpictureurl";
        public static final String Type = "promotiontype";
        public static final String StartDate = "promotionstartdate";
        public static final String EndDate = "promotionenddate";
        public static final String PromotionDetail = "promotiondetail";
        public static final String Location = "promotionlocation";
        public static final String Userid = "promotionuserid";
        public static final String Resid = "promotionresid";
    }

    private String promotionid;
    private String promotionname;
    private String promotionstartdate;
    private String promotionenddate;
    private String promotiondetail;
    private String promotionlocation;
//    private String promotiontype;
    private String promotionpictureurl;
    private String promotionuserid;
    private String resid;
    // Constructor

    public Promotion(String promotionname, String promotionpictureurl, String promotionstartdate, String promotionenddate, String promotiondetail ,String promotionlocation) {
        this.promotionname = promotionname;
        this.promotionstartdate = promotionstartdate;
        this.promotionenddate = promotionenddate;
        this.promotiondetail = promotiondetail;
//        this.promotiontype = promotiontype;
        this.promotionpictureurl = promotionpictureurl;
        this.promotionlocation = promotionlocation;
    }

    //region getter setter

    public String getResid(){ return  resid;}

    public void setResid(String resid){ this.resid = resid; }

    public String getUserid(){ return  promotionuserid;}

    public void setUserid(String promotionuserid){ this.promotionuserid = promotionuserid; }

    public String getId() {
        return promotionid;
    }

    public void setId(String promotionid) {
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
                "promotionid='" + promotionid + '\'' +
                ", promotionname='" + promotionname + '\'' +
                ", promotionstartdate='" + promotionstartdate + '\'' +
                ", promotionenddate='" + promotionenddate + '\'' +
                ", promotiondetail='" + promotiondetail + '\'' +
                ", promotionlocation='" + promotionlocation + '\'' +
                ", promotionpictureurl='" + promotionpictureurl + '\'' +
                ", promotionuserid='" + promotionuserid + '\'' +
                '}';
    }
}
