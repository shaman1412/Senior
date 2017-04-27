package com.Senior.Faff.Fragment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SearchViewCompat;

import com.Senior.Faff.Fragment.SearchNearby.Nearby_Fragment;
import com.Senior.Faff.Fragment.SearchNearby.Search_Fragment;

/**
 * Created by InFiNity on 16-Feb-17.
 */

public class Search_adapter extends FragmentPagerAdapter {
    private Context context;
    private Bundle bundle;
    private final int PAGE_NUM = 2;
    private String Tabtitle[] = new String[] {"Seach","Nearby"};
    public Search_adapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

            return new Search_Fragment();
        }else if(position == 1) {
            Nearby_Fragment nearby_fragment = new Nearby_Fragment();
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
