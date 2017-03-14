package com.Senior.Faff.model;

import android.provider.BaseColumns;

/**
 * Created by InFiNity on 21-Jan-17.
 */

public class Marker {
    public static final String TABLE = "Marker";
    public class column{
        public static  final String ID = BaseColumns._ID;
        public static final String TITLE="Title";
        public static  final  String SNIPPET = "Snippet";
        public static final  String POSITION = "Position";
    }
    private int id;
    private String title;
    private String snippet;
    private String position;

    public Marker(String title,String snippet,String position){
        this.title = title;
        this.snippet = snippet;
        this.position = position;
    }
    public Marker(){


    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getMarkerTitle(){
        return title;
    }
    public String getMarkerSnippet(){
        return snippet;
    }
    public String getMarkerPostion(){
        return  position;
    }
    public void setMarkerTitle(String title){
        this.title = title;
    }
    public void setMarketSnipper(String snippet){
        this.snippet = snippet;
    }
    public void setMarketPosition(String position){
        this.position = position;
    }
}
