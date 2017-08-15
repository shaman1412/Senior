package com.Senior.Faff.Fragment.Adapter;

/**
 * Created by InFiNity on 09-Feb-17.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Senior.Faff.Fragment.Home.PromotionRestaurantMainFragment;
import com.Senior.Faff.Fragment.Home.TopResturantMainFragment;
import com.Senior.Faff.Fragment.Home.AdviceRestaurantMainFragment;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserProfile;

public class HomeAdpater extends FragmentPagerAdapter   {
    private final int PAGE_NUM = 3;
    private String tabTitles[] = new String[] { "ร้านอาหารแนะนำ", "แนะนำโปรโมชั่น", "ร้านอาหารยอดนิยม" };
    private Context context;
    private String userid;
    private int[] imageResId = {
            R.drawable.find_res,
            R.drawable.find_res,
            R.drawable.find_res
    };
    public HomeAdpater(FragmentManager fm, Context context, String userid) {
        super(fm);
        this.context = context;
        this.userid = userid;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            Bundle b = new Bundle();
            b.putString("userid", userid);
            AdviceRestaurantMainFragment ad = new AdviceRestaurantMainFragment();
            ad.setArguments(b);
            return ad;
        }
        else if(position == 1) {
            Bundle b = new Bundle();
            b.putString(UserProfile.Column.UserID, userid);
            PromotionRestaurantMainFragment rm = new PromotionRestaurantMainFragment();
            rm.setArguments(b);
            return rm;
        }
        else if(position == 2){
            Bundle b = new Bundle();
            b.putString("userid", userid);
            TopResturantMainFragment tr = new TopResturantMainFragment();
            tr.setArguments(b);
            return  tr;
        }
        else
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
         return tabTitles[position];

        // getDrawable(int i) is deprecated, use getDrawable(int i, Theme theme) for min SDK >=21
        // or ContextCompat.getDrawable(Context context, int id) if you want support for older versions.
        //Drawable image = context.getResources().getDrawable(iconIds[position], context.getTheme());
        // Drawable image = context.getResources().getDrawable(imageResId[position]);

     /*    Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;*/
    }

}
