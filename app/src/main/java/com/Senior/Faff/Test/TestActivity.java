package com.Senior.Faff.Test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.Senior.Faff.utils.DownloadCallback;

public class TestActivity extends FragmentActivity implements DownloadCallback {

    private TextView a;
    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyBQFg2lwotBcSxfXrg6vQDhOrrdrn2rhBM");
    }
        // Boolean telling us whether a download is in progress, so we don't trigger overlapping
        // downloads with consecutive button clicks.

    private void startDownload() {
            if (!mDownloading && mNetworkFragment != null) {
                // Execute the async download.
                mNetworkFragment.startDownload();
                mDownloading = true;
            }
    }

    @Override
    public void updateFromDownload(Object result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:

                break;
            case Progress.CONNECT_SUCCESS:

                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }


}
