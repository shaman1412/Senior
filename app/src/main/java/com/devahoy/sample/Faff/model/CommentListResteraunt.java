package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 1/6/2017.
 */

import android.provider.BaseColumns;

public class CommentListResteraunt {

    public static final String TABLE = "comment_list_resteraunt";

    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String CommentID = "CommentID";
        public static final String ResterauntID = "ResterauntID";
    }

    private int id;
    private int commentID;
    private int resterauntID;

    public CommentListResteraunt(int commentID, int resterauntID){
        this.commentID = commentID;
        this.resterauntID = resterauntID;
    }

    public CommentListResteraunt(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getResterauntID() {
        return resterauntID;
    }

    public void setResterauntID(int resterauntID) {
        this.resterauntID = resterauntID;
    }

    //endregion

}
