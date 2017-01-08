package com.example.comi.tablayoutv1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Comi on 2015/10/22.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final int COUNT = 5;
    private String[] titles = new String[]{"二頭肌", "三頭肌", "肩膀前束", "肩膀後束", "側三角肌"};
    private Context context;
    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        return MainActivityFragment.newInstance(position + 1);

    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}