package com.Senior.sample.Faff.Fragment.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.sample.Faff.Fragment.InsertUserProfile.Edit_profile;
import com.Senior.sample.Faff.Fragment.InsertUserProfile.Signup_profile;

/**
 * Created by InFiNity on 14-Feb-17.
 */

public class InsertUserProfile_adapter extends FragmentPagerAdapter{
    private final int PAGE_NUM  = 2;
    public InsertUserProfile_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new Signup_profile();
        }
        else if(position == 1){
            return new Edit_profile();
        }
        else
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
