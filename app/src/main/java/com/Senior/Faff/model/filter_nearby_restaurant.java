package com.Senior.Faff.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by InFiNity on 26-Apr-17.
 */

public class filter_nearby_restaurant implements Serializable {

    private String type = "all" ;
    private String distnace = "70";

    public filter_nearby_restaurant(ArrayList<String> type, Float distance){
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
    public filter_nearby_restaurant(){

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
