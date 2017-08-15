package com.Senior.Faff.Fragment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.Faff.Fragment.SearchNearby.NearbyFragment;
import com.Senior.Faff.Fragment.SearchNearby.SearchFragment;

/**
 * Created by InFiNity on 16-Feb-17.
 */

public class SearchAdapter extends FragmentPagerAdapter {
    private Context context;
    private Bundle bundle;
    private final int PAGE_NUM = 2;
    private String Tabtitle[] = new String[] {"Seach","Nearby"};
    public SearchAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

            return new SearchFragment();
        }else if(position == 1) {
            NearbyFragment nearby_fragment = new NearbyFragment();
            nearby_fragment.setArguments(bundle);
            return nearby_fragment;
        }
        else
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position){
        return    Tabtitle[position];
    }
}
