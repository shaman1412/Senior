package com.devahoy.sample.Faff.model;

/**
 * Created by Not_Today on 12/23/2016.
 */

import android.provider.BaseColumns;

public class TypeFood {
    public static final String TABLE = "type";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String Type = "Type";
    }

    private int id;
    private String type;

    public TypeFood(String type){
        this.type = type;
    }

    public TypeFood()
    {

    }

    //region getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //endregion
}
