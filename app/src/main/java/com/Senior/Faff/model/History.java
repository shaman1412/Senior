package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class History {

    public static final String TABLE = "history";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UserID = "UserID";
        public static final String PartyID = "PartyID";
    }

    private int id;
    private int userID;
    private int partyID;

    public History(int userID, int partyID){
        this.userID = userID;
        this.partyID = partyID;
    }

    public History(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPartyID() {
        return partyID;
    }

    public void setPartyID(int partyID) {
        this.partyID = partyID;
    }

    //endregion
}
