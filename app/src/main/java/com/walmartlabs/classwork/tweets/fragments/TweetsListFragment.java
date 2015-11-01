package com.walmartlabs.classwork.tweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.codepath.apps.tweets.R;
import com.walmartlabs.classwork.tweets.adapters.TweetsAdapter;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.util.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abalak5 on 10/30/15.
 */
public abstract class TweetsListFragment extends Fragment{
    private TweetsAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
/*                RequestParams params = new RequestParams();
                params.put("count", 25);
                params.put("max_id", maxId);*/
                fetchAndPopulateTimeline(false);
                return true;
            }
        });
        
        lvTweets.setAdapter(aTweets);


        /*swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                params.put("count", 25);
                params.put("since_id", 1);
                Log.i("sinceId", Long.toString(sinceId));
                fetchAndUpdateFeed(params);
            }
        });*/
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

    protected abstract void fetchAndPopulateTimeline(boolean clearCache);
}
