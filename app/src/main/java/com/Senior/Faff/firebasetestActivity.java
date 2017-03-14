package com.Senior.Faff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.Senior.Faff.utils.firebase;

public class firebasetestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasetest);

        firebase a  = new firebase();
        a.writeNewUser();
    }
}
