package com.Senior.sample.Faff.Fragment.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.sample.Faff.Fragment.Party.Created_Fragment;
import com.Senior.sample.Faff.Fragment.Party.Joined_Fragment;
import com.Senior.sample.Faff.Fragment.Party.Requested_Fragment;
import com.Senior.sample.Faff.Fragment.Party.Room_fragment;

/**
 * Created by InFiNity on 13-Feb-17.
 */

public class Party_adapter extends FragmentPagerAdapter {
    private final int PAGE_NUM = 4;
    private String[] Tabtitle = new String[] {"Room","joined","Requested","Created"};
    private Context mcontext;
    public Party_adapter(FragmentManager fm, Context context) {
        super(fm);
        this.mcontext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return  new Room_fragment();
        }
        else if(position == 1){
            return  new Joined_Fragment();
        }
        else if(position == 2){
            return  new Requested_Fragment();
        }
        else if(position == 3){
            return  new Created_Fragment();
        }
        else
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
    @Override
    public CharSequence  getPageTitle(int position){
        return Tabtitle[position];
    }
}
