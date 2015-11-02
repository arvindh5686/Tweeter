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
        fetchAndPopulateTimeline();
    }

    @Override
    public void fetchAndPopulateTimeline() {
        if (isNetworkAvailable()) {
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
