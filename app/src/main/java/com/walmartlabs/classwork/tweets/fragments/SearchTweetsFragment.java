package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;

import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

/**
 * Created by abalak5 on 10/31/15.
 */
public class SearchTweetsFragment extends TweetsListFragment {

    private TwitterClient client;

    public static SearchTweetsFragment newInstance(String query) {
        SearchTweetsFragment fragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
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
            String query = getArguments().getString("query");
            client.getSearchResults(sinceId, maxId, query, getHandler());
        } else {
            getFromCache();
        }
    }
}
