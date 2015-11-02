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
public class MentionsTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    public static long maxId;
    public static long sinceId = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        //setupListView();
        sinceId = 1;
        maxId = 0;
        fetchAndPopulateTimeline(true);
    }

    @Override
    public void fetchAndPopulateTimeline(boolean clearCache) {
        if (isNetworkAvailable()) {
            if (clearCache) {
                sinceId = 1;
                maxId = 0;
                clear();
            }
            client.getMentionsTimeline(sinceId, maxId, new JsonHttpResponseHandler() {
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
