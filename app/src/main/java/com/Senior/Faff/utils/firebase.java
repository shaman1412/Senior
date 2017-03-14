package com.Senior.Faff.utils;

import com.Senior.Faff.model.UserAuthen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by InFiNity on 14-Mar-17.
 */

public class firebase {
   public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
   public DatabaseReference mMessagesRef = mRootRef.child("messages");
   public DatabaseReference mUsersRef = mRootRef.child("users");


    public void writeNewUser() {
        UserAuthen friendlyMessage = new UserAuthen("Hello World!", "Jirawatee");
        mUsersRef.child("id-12345").setValue("Jirawatee");
        //mMessagesRef.child(friendlyMessage)
    }



}
