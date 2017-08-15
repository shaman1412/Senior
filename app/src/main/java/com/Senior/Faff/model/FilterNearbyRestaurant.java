package com.Senior.Faff.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by InFiNity on 26-Apr-17.
 */

public class FilterNearbyRestaurant implements Serializable {

    private String type = "all" ;
    private String distnace = "70";

    public FilterNearbyRestaurant(ArrayList<String> type, Float distance){
        boolean check = true;
        StringBuilder sb = new StringBuilder();
        for (String s : type)
        {
            if(check) {
                sb.append(s);
                check = false;
            }else {
                sb.append(",");
                sb.append(s);
            }
        }
        this.type = sb.toString();
        this.distnace = String.valueOf(distance);

    }
    public FilterNearbyRestaurant(){

    }
    public String getType(){
        return type;
    }
    public void setType(ArrayList<String> type){
        boolean check = true;
        StringBuilder sb = new StringBuilder();
        for (String s : type)
        {
            if(check) {
                sb.append(s);
                check = false;
            }else {
                sb.append(",");
                sb.append(s);
            }
        }
        this.type = sb.toString();
    }
    public String getDistance(){
        return distnace;
    }
    public void setDistnace(Float distance){
        this.distnace = String.valueOf(distance);
    }

}
