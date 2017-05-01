package com.Senior.Faff.Fragment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.Senior.Faff.Fragment.Party.Created_Fragment;
import com.Senior.Faff.Fragment.Party.Joined_Fragment;
import com.Senior.Faff.Fragment.Party.Requested_Fragment;
import com.Senior.Faff.Fragment.Party.Room_fragment;
import com.Senior.Faff.chat.CreateParty;

/**
 * Created by InFiNity on 13-Feb-17.
 */

public class Party_adapter extends FragmentPagerAdapter {
    private final int PAGE_NUM = 4;
    private String[] Tabtitle = new String[]{"Room", "joined", "Requested", "Created"};
    private Context mcontext;
    private Bundle bundle;

    public Party_adapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        this.mcontext = context;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            debug("In party adapter postion 0");
            Room_fragment room = new Room_fragment();
            room.setArguments(bundle);
            return room;
        } else if (position == 1) {
            debug("In party adapter postion 1");
            Joined_Fragment join = new Joined_Fragment();
            join.setArguments(bundle);
            return join;
        } else if (position == 2) {
            debug("In party adapter postion 2");
            Requested_Fragment request = new Requested_Fragment();
            request.setArguments(bundle);
            return request;
        } else if (position == 3) {
            debug("In party adapter postion 3");
            Created_Fragment create = new Created_Fragment();
            create.setArguments(bundle);
            return create;
        } else
            return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tabtitle[position];
    }

    private void debug(String d) {
        Log.i("TEST:", " debug : " + d);
    }

}
