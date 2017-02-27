package com.troy.xifan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.orhanobut.logger.Logger;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/17.
 */

public class SearchPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] tabTitles;

    public SearchPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitles) {
        super(fm);
        Logger.d("SearchPagerAdapter:");
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Logger.d("getItem:"+fragments.get(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        Logger.d("fragments:"+fragments.size());
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
