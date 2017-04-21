package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class BookmarkList {

    public static final String TABLE = "bookmark_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String BookmarkID = "BookmarkID";
        public static final String UserID = "UserID";
    }

    private int id;
    private String bookmarkID;
    private String userID;

    public BookmarkList(String userID){
        this.userID = userID;
    }

    public BookmarkList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookmarkID() {
        return bookmarkID;
    }

    public void setBookmarkID(String bookmarkID) {
        this.bookmarkID = bookmarkID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //endregion
}
