package com.Senior.Faff.utils;

import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.UserAuthen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by InFiNity on 15-Mar-17.
 */

public class CreatePartyManager {

    private String name;
    private String description;
    private int people;
    private String roomid;
    private String location;
    private String appointment;
    private String userid;
    private String request;
    private String accept;
    private String address;
    private String telephone;
    private String rule_age ;
    private String rule_gender;
    private String createby;
    private Map<String, String> rule = new HashMap<>();
    public CreatePartyManager(String roomid,String name,  String description, int people, String address ,String appointment, String telephone,String location, String rule_age, String rule_gender,String userid, String createby){
        this.name = name;
        this.roomid = roomid;
        this.description = description;
        this.people = people;
        this.address = address;
        this.appointment  = appointment;
        this.telephone = telephone;
        this.location = location;
        this.rule_age= rule_age;
        this.rule_gender = rule_gender;
        this.userid = userid;
        this.createby = createby;
        addhash();
    }
    public void addhash() {
        if(rule_gender != null) {
            rule.put("gender", rule_gender);
        }
        if(rule_age != null){
            rule.put("age", rule_age);
        }
    }



    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference allroom = mdatabase.child("All_Room");


    public void addroom() {

        Party party = new Party(roomid, name, description, people, address, appointment, telephone);
        party.setLocation(location);
        party.setCreateid(userid);
        party.setCreatename(createby);
        party.setRule(rule);
      allroom.push().setValue(party);
    }

}
