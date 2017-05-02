package com.Senior.Faff.model;

import android.provider.BaseColumns;

/**
 * Created by Not_Today on 5/2/2017.
 */

public class Restaurant_Promotion {

    public static final String TABLE = "Restaurant_Promotion";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String resid = "resid";
        public static final String promotionid = "promotionid";
    }

    private String resid;
    private String promotionid;

    public Restaurant_Promotion(String resid, String promotionid) {
        this.resid = resid;
        this.promotionid = promotionid;
    }

    public Restaurant_Promotion(){

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
