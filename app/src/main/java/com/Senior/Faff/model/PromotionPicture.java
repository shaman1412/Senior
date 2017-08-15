package com.Senior.Faff.Model;

/**
 * Created by Not_Today on 2/11/2017.
 */

import android.provider.BaseColumns;

public class PromotionPicture {
    public static final String TABLE = "promotion_picture";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String PromotionID = "PromotionID";
        public static final String PictureData = "PictureData";
    }

    private int proid;
    private int id;
    private byte[] pictureData;

    public PromotionPicture(int proid, byte[] pictureData)
    {
        this.proid = proid;
        this.pictureData = pictureData;
    }

    //region gettter setter

    public int getProid() {
        return proid;
    }

    public void setProid(int proid) {
        this.proid = proid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    //endregion
}
