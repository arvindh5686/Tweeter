package com.walmartlabs.classwork.tweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.walmartlabs.classwork.tweets.fragments.HomeTimelineFragment;
import com.walmartlabs.classwork.tweets.fragments.MentionsTimelineFragment;

/**
 * Created by abalak5 on 10/31/15.
 */
//return the order of the fragments in the view pager
public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1){
            return new MentionsTimelineFragment();
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