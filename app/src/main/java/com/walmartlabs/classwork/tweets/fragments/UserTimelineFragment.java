package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;

import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

/**
 * Created by abalak5 on 10/31/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Override
    public void fetchAndPopulateTimeline() {
        if (isNetworkAvailable()) {
            showProgressBar();
            String screenName = getArguments().getString("screen_name");
            client.getUserTimeline(sinceId, maxId, screenName, getHandler());
        } else {
            getFromCache();
        }
    }
}
