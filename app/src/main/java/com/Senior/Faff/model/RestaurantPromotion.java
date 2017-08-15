package com.Senior.Faff.model;

import android.provider.BaseColumns;

/**
 * Created by Not_Today on 5/2/2017.
 */

public class RestaurantPromotion {

    public static final String TABLE = "RestaurantPromotion";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String resid = "resid";
        public static final String promotionid = "promotionid";
    }

    private String resid;
    private String promotionid;

    public RestaurantPromotion(String resid, String promotionid) {
        this.resid = resid;
        this.promotionid = promotionid;
    }

    public RestaurantPromotion(){

    }

    //region Getter Setter
    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getPromotionid() {
        return promotionid;
    }

    public void setPromotionid(String promotionid) {
        this.promotionid = promotionid;
    }
    //endregion
}
