package com.Senior.Faff.utils;

import com.Senior.Faff.model.UserAuthen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InFiNity on 14-Mar-17.
 */

public class firebase {
   public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
   public DatabaseReference mMessagesRef = mRootRef.child("messages");
   public DatabaseReference mUsersRef = mRootRef.child("users");
    public DatabaseReference t = mRootRef.child("test");

    public DatabaseReference ah;


    public void writeNewUser() {
      UserAuthen friendlyMessage = new UserAuthen("Hello World!", "Jirawatee");
     // mUsersRef.child("id-12345").setValue("Jirawatee");
      //  mMessagesRef.child("1").setValue(friendlyMessage);
       // mMessagesRef.child("2").setValue(friendlyMessage);
        HashMap<String, Object> result = new HashMap<>();
        result.put("password", "123");
        result.put("username", "234");

        t.push().setValue(result);
        t.push().setValue(result);

    }
    public  void change(){
        //String key = mMessagesRef.push().getKey();
        String key = "12";
        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("password", "shit");
        postValues.put("username", "ddddd!");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/users" + key, postValues);
        childUpdates.put("/user-messages/Jirawatee/" + key, postValues);

        mRootRef.updateChildren(childUpdates);

    }

}
