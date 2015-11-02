package com.walmartlabs.classwork.tweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmartlabs.classwork.tweets.activities.DetailedViewActivity;
import com.walmartlabs.classwork.tweets.adapters.TweetsAdapter;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.util.EndlessScrollListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abalak5 on 10/30/15.
 */
public abstract class TweetsListFragment extends Fragment{
    private TweetsAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    protected static long maxId;
    protected static long sinceId = 1;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sinceId = 1;
        maxId = 0;
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsAdapter(getActivity(), tweets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchAndPopulateTimeline();
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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sinceId = 1;
                maxId = 0;
                aTweets.clear();
                fetchAndPopulateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        
        lvTweets.setAdapter(aTweets);

        return view;
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void getFromCache() {
        List<Tweet> tweets  = Tweet.recentItems();
        aTweets.clear();
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
    }

    public void clearCache() {
        new Delete().from(Tweet.class).execute();
        new Delete().from(User.class).execute();
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clear() {
        aTweets.clear();
    }

    protected abstract void fetchAndPopulateTimeline();

    public void onFinishEditDialog(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
    }

    public JsonHttpResponseHandler getHandler() {
        JsonHttpResponseHandler handler =  new JsonHttpResponseHandler() {
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
        };

        return handler;
    }
}
