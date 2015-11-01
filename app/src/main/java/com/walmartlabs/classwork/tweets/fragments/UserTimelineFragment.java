package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abalak5 on 10/31/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private static long maxId;
    private static long sinceId = 1;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        sinceId = 1;
        maxId = 0;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        //setupListView();
        fetchAndPopulateTimeline(true);
    }

/*
    private void setupListView() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                RequestParams params = new RequestParams();
                params.put("count", 25);
                params.put("max_id", maxId);
                fetchAndPopulateTimeline(params, true);
                return true;
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailedViewActivity.class);
                Tweet tweet = (Tweet) aTweets.getItem(position);
                i.putExtra("tweet", tweet);
                startActivity(i);
            }
        });
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
            String screenName = getArguments().getString("screen_name");
            client.getUserTimeline(sinceId, maxId, screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
                    if (tweets.size() > 0) {
                        //get last tweets uid and subtract one as max_id is inclusive
                        maxId = tweets.get(tweets.size() - 1).getUid() - 1;
                    }
                    addAll(tweets);
                    Log.d("Success", response.toString());
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
