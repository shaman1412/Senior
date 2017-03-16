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

    private String name,description;
    private int people;
    //private String rule;
    private String roomID;
    private String location;
    private Map<String, String> rule = new HashMap<>();
    public CreatePartyManager(String roomID, String name, String description, int people, int[] rule,String location ){
        this.name  = name;
        this.description = description;
        this.people = people;
        //this.rule  = Arrays.toString(rule);
        addhash();
        this.location = location;
        this.roomID = roomID;
    }
    public void addhash() {
        rule.put("password", "shit");
        rule.put("username", "ddddd!");
    }

    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference allroom = mdatabase.child("All_Room");


    public void addroom() {

        Party party = new Party(roomID,name,description,people,rule,location);
      allroom.push().setValue(party);
    }

}
