package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;

import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

/**
 * Created by abalak5 on 10/31/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        clear();
        fetchAndPopulateTimeline();
    }

    @Override
    public void fetchAndPopulateTimeline() {
        if (isNetworkAvailable()) {
            client.getHomeTimeline(sinceId, maxId, getHandler());
        } else {
            getFromCache();
        }
    }


}
