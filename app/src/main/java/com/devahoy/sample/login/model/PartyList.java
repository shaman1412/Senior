package com.devahoy.sample.login.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class PartyList {

    public static final String TABLE = "party_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String PartyID = "PartyID";
        public static final String UserID = "UserID";
    }

    private int id;
    private int partyID;
    private int userID;

    public PartyList(int partyID, int userID){
        this.partyID = partyID;
        this.userID = userID;
    }

    public PartyList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartyID() {
        return partyID;
    }

    public void setPartyID(int partyID) {
        this.partyID = partyID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
