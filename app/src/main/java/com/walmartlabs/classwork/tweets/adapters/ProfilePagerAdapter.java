package com.walmartlabs.classwork.tweets.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.walmartlabs.classwork.tweets.fragments.FavouritesFragment;
import com.walmartlabs.classwork.tweets.fragments.UserTimelineFragment;

/**
 * Created by abalak5 on 10/31/15.
 */
//return the order of the fragments in the view pager
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Tweets", "Favourties" };
    private Bundle args;

    public ProfilePagerAdapter(FragmentManager fm, Bundle args) {
        super(fm);
        this.args = args;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return UserTimelineFragment.newInstance(this.args);
        } else if (position == 1){
            return FavouritesFragment.newInstance(this.args);
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}