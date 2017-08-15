package com.Senior.Faff.Test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.Senior.Faff.R;
import com.Senior.Faff.ServerService;

public class TestCallservice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_callservice);

        Intent intent = new Intent(this, ServerService.class);
        startService(intent);


    }

}
