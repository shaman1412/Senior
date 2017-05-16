package com.Senior.Faff.utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Senior.Faff.R;

public class LoadingFragment extends Fragment {

    Handler handler;
    Runnable runnable;
    Long delay_time;
    String to_class;
    Class<?> to;
    Long time = 20000L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.activity_splash, container, false);

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                Bundle from = getArguments();
                if (from.getString("to")!=null) {
                    to_class = from.getString("to");
                    try {
                        to = Class.forName(to_class);
                        Intent intent = new Intent(LoadingFragment.this.getActivity(), to);
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
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
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}