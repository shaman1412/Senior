package com.Senior.Faff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class Main3Activity extends AppCompatActivity {
    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "playerLevel";


    static final int DAY_VIEW_MODE = 0;
    static final int WEEK_VIEW_MODE = 1;

    private SharedPreferences mPrefs;
    private int mCurViewMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button a = (Button)findViewById(R.id.h);

        SharedPreferences mPrefs = getSharedPreferences("view_mode" ,MODE_PRIVATE);
        mCurViewMode = mPrefs.getInt("view_mode", DAY_VIEW_MODE);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String  mCurrentScore = savedInstanceState.getString(STATE_SCORE);
            Toast.makeText(this,  mCurrentScore , Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();

        Log.d(TAG, " b onCreate ");
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //finish();
                Intent k = new Intent(Main3Activity.this,testlife.class);
                startActivity(k);
            }
        });


    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        EditText b = (EditText)findViewById(R.id.k);
        String f = b.getText().toString();
        savedInstanceState.putString(STATE_SCORE, "adsasd");
        super.onSaveInstanceState(savedInstanceState);


        // Always call the superclass so it can save the view hierarchy state

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "b onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " b onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences mPrefs = getSharedPreferences("view_mode" ,MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("view_mode", 3);
        ed.commit();
        Log.d(TAG, "b onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "b onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "b onRestar: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "b onStart: ");
    }
}
