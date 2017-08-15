package com.Senior.Faff.Fragment.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.Faff.Fragment.InsertUserProfile.EditProfile;
import com.Senior.Faff.Fragment.InsertUserProfile.SignupProfile;

/**
 * Created by InFiNity on 14-Feb-17.
 */

public class InsertUserProfileAdapter extends FragmentPagerAdapter{
    private final int PAGE_NUM  = 2;
    public InsertUserProfileAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new SignupProfile();
        }
        else if(position == 1){
            return new EditProfile();
        }
        else
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
