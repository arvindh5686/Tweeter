package com.walmartlabs.classwork.tweets.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walmartlabs.classwork.tweets.adapters.TweetsAdapter;
import com.walmartlabs.classwork.tweets.fragments.EditNameDialog;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;
import com.walmartlabs.classwork.tweets.util.EndlessScrollListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity implements EditNameDialog.EditNameDialogListener {

    private TwitterClient client;
    private TweetsAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    public static long maxId;
    public static long sinceId;
    private EditNameDialog editNameDialog;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.setLoggingEnabled(true);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsAdapter(this, tweets);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        setupListView();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                params.put("count", 25);
                params.put("since_id", 1);
                Log.i("sinceId", Long.toString(sinceId));
                fetchAndUpdateFeed(params);
            }
        });

        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);

        fetchAndPopulateTimeline(params);
    }

    private void setupListView() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4FAAF1")));

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                RequestParams params = new RequestParams();
                params.put("count", 25);
                params.put("max_id", TimelineActivity.maxId);
                fetchAndPopulateTimeline(params);
                return true;
            }
        });
    }

    private void fetchAndUpdateFeed(RequestParams params) {
        if(isNetworkAvailable()) {
            client.getTimeline(params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (response.length() == 0) return;
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
                    aTweets.clear();
                    aTweets.addAll(tweets);
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

    }

    private void fetchAndPopulateTimeline(RequestParams params) {
        if (isNetworkAvailable()) {
            client.getTimeline(params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    ArrayList<Tweet> tweets = Tweet.fromJsonArray(response);
                    aTweets.addAll(tweets);
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

    public void getFromCache() {
        List<Tweet> tweets  = new Select()
                .from(Tweet.class)
                .orderBy("uid DESC")
                .execute();
        aTweets.clear();
        aTweets.notifyDataSetChanged();
        aTweets.addAll(tweets);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose_tweet) {
            FragmentManager fm = getSupportFragmentManager();
            editNameDialog = EditNameDialog.newInstance("Some Title");
            editNameDialog.show(fm, "fragment_compose_tweet");
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
    }
}
