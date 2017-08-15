package com.Senior.Faff.Model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class Score {
    public static final String TABLE = "score";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "UserID";
        public static final String RestaurantID = "RestaurantID";
        public static final String Score = "Score";
    }

    private int id;
    private int userID;
    private int restaurantID;
    private int score;

    public Score(int userID, int restaurantID, int score){
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.score = score;
    }

    public Score(){

    }

    //region getter setter

    //endregion
}
