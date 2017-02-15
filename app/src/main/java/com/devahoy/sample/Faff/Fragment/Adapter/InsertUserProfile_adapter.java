package com.devahoy.sample.Faff.Fragment.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.devahoy.sample.Faff.Fragment.InsertUserProfile.Second_page;
import com.devahoy.sample.Faff.Fragment.InsertUserProfile.first_page;

/**
 * Created by InFiNity on 14-Feb-17.
 */

public class InsetUserProfile_adapter extends FragmentPagerAdapter{
    private final int PAGE_NUM  = 2;
    public InsetUserProfile_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new first_page();
        }
        else if(position == 1){
            return new Second_page();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
