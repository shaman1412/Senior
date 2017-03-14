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
    private int bookmarkID;
    private int userID;

    public BookmarkList(int bookmarkID, int userID){
        this.bookmarkID = bookmarkID;
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

    public int getBookmarkID() {
        return bookmarkID;
    }

    public void setBookmarkID(int bookmarkID) {
        this.bookmarkID = bookmarkID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
