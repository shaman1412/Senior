package com.Senior.Faff.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.Senior.Faff.Model.Party;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
    private String url_pic;
    private Uri uri_pic;
    private String pic_name;
    private StorageMetadata pic_content_type;

    private Map<String, String> rule = new HashMap<>();
    public CreatePartyManager(String roomid,String name,  String description, int people, String address ,String appointment, String telephone,String location, String rule_age, String rule_gender,String userid, String createby, String picture){
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
        ArrayList<String> url_pic_arr = new Gson().fromJson(picture, ArrayList.class);
        this.url_pic = url_pic_arr.get(0);
        uri_pic = Uri.fromFile(new File(url_pic));
        String[] st = url_pic.split("/");
        String tmp = st[st.length-1];
        pic_name = new Date().toString()+"-----"+tmp;
        Log.i("TEST: ", "pic name is : "+pic_name);
        Log.i("TEST: ", "st[st.length-1] : "+tmp);
        String[] arr_ext = tmp.split("\\.");
        if(arr_ext[arr_ext.length-1].toLowerCase().equals("png"))
        {
            pic_content_type = new StorageMetadata.Builder().setContentType("image/png").build();
        }
        else if (arr_ext[arr_ext.length-1].toLowerCase().equals("jpg"))
        {
            pic_content_type = new StorageMetadata.Builder().setContentType("image/jpeg").build();
        }
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

    public void addroom() {
        final Party party = new Party(roomid, name, description, people, address, appointment, telephone);
        party.setLocation(location);
        party.setCreateid(userid);
        party.setCreatename(createby);
        party.setRule(rule);
        party.setAccept(userid);
        party.setPicture("/"+roomid+"/"+pic_name);
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        final StorageReference party_pic = storage.child("/"+this.roomid+"/"+this.pic_name);
        UploadTask uploadTask = party_pic.putFile(uri_pic, pic_content_type);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                party.setPicture(party_pic.getDownloadUrl().toString());
            }
        });

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference allroom = mdatabase.child("All_Room");
        allroom.push().setValue(party);
    }

}
