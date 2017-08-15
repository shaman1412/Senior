package com.Senior.Faff.Fragment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.Senior.Faff.Fragment.Party.CreatedFragment;
import com.Senior.Faff.Fragment.Party.JoinedFragment;
import com.Senior.Faff.Fragment.Party.RequestedFragment;
import com.Senior.Faff.Fragment.Party.RoomFragment;

/**
 * Created by InFiNity on 13-Feb-17.
 */

public class PartyAdapter extends FragmentPagerAdapter {
    private final int PAGE_NUM = 4;
    private String[] Tabtitle = new String[]{"Room", "joined", "Requested", "Created"};
    private Context mcontext;
    private Bundle bundle;

    public PartyAdapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        this.mcontext = context;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            debug("In party adapter postion 0");
            RoomFragment room = new RoomFragment();
            room.setArguments(bundle);
            return room;
        } else if (position == 1) {
            debug("In party adapter postion 1");
            JoinedFragment join = new JoinedFragment();
            join.setArguments(bundle);
            return join;
        } else if (position == 2) {
            debug("In party adapter postion 2");
            RequestedFragment request = new RequestedFragment();
            request.setArguments(bundle);
            return request;
        } else if (position == 3) {
            debug("In party adapter postion 3");
            CreatedFragment create = new CreatedFragment();
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
