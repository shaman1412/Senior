package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 2/11/2017.
 */

public class PromotionPicture {
    public static final String TABLE = "promotion_picture";

    public class Column {
        public static final String UserID = "UserID";
        public static final String PictureID = "PictureID";
        public static final String PictureData = "PictureData";
    }

    private int pid;
    private int uid;
    private Byte[] pictureData;

    public PromotionPicture(int uid, int pid, Byte[] pictureData)
    {
        this.uid = uid;
        this.pid = pid;
        this.pictureData = pictureData;
    }
}
