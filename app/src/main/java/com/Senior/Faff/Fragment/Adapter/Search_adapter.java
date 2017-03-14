package com.Senior.Faff.Fragment.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.Faff.Fragment.SearchNearby.Nearby_Fragment;
import com.Senior.Faff.Fragment.SearchNearby.Search_Fragment;

/**
 * Created by InFiNity on 16-Feb-17.
 */

public class Search_adapter extends FragmentPagerAdapter {
    private Context context;
    private final int PAGE_NUM = 2;
    private String Tabtitle[] = new String[] {"Seach","Nearby"};
    public Search_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new Search_Fragment();
        }else if(position == 1)
            return new  Nearby_Fragment();
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
