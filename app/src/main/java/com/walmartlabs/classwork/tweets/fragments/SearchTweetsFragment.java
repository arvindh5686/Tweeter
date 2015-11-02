package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abalak5 on 10/31/15.
 */
public class SearchTweetsFragment extends TweetsListFragment {

    private TwitterClient client;
    public static long maxId;
    public static long sinceId = 1;

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
        //setupListView();
        sinceId = 1;
        maxId = 0;
        fetchAndPopulateTimeline(true);
    }

/*
    private void setupListView() {

    }*/

/*    private void fetchAndUpdateFeed(RequestParams params) {
        if(isNetworkAvailable()) {
            clearCache();
            client.getTimeline(params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (response.length() == 0) return;
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
                    aTweets.clear();
                    addAll(tweets);
                    Log.d("Success", response.toString());
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Failure", errorResponse.toString());
                    swipeContainer.setRefreshing(false);
                }
            });
        } else {
            getFromCache();
            swipeContainer.setRefreshing(false);
        }

    }*/

    @Override
    public void fetchAndPopulateTimeline(boolean clearCache) {
        if (isNetworkAvailable()) {
            if (clearCache) {
                sinceId = 1;
                maxId = 0;
                clear();
            }

            String query = getArguments().getString("query");
            client.getSearchResults(sinceId, maxId, query, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    ArrayList<Tweet> tweets = null;
                    try {
                        tweets = Tweet.fromJsonArray(response.getJSONArray("statuses"));
                        if (tweets.size() > 0) {
                            //get last tweets uid and subtract one as max_id is inclusive
                            maxId = tweets.get(tweets.size() - 1).getUid() - 1;
                        }
                        addAll(tweets);
                        Log.d("Success", response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Failure", errorResponse.toString());
                }
            });
        } else {
            getFromCache();
        }
    }
}
