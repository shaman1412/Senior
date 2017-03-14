package com.Senior.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class ReportList {
    public static final String TABLE = "report_list";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String ReportID = "ReportID";
        public static final String UserID = "UserID";
    }

    private int id;
    private int reportID;
    private int userID;

    public ReportList(int reportID, int userID){
        this.reportID = reportID;
        this.userID = userID;
    }

    public ReportList(){

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //endregion
}
