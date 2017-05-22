package com.Senior.Faff.utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.R;

public class LoadingFragment extends Fragment {

    Handler handler;
    Runnable runnable;
    Long delay_time;
    Long time = 20000L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.activity_splash, container, false);

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                    onStop();
            }
        };

        return  root;
    }

    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onStop() {
        super.onStop();
        if(runnable!=null)
        {
            try {
                handler.removeCallbacks(runnable);
                time = delay_time - (System.currentTimeMillis() - time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}