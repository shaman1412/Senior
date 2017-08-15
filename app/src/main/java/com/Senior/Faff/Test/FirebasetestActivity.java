package com.Senior.Faff.Test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.Senior.Faff.R;
import com.Senior.Faff.utils.Firebase;

public class FirebasetestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasetest);

        Firebase a  = new Firebase();
        a.writeNewUser();
    }
}
