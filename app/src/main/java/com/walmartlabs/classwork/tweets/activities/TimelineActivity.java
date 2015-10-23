package com.walmartlabs.classwork.tweets.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walmartlabs.classwork.tweets.adapters.TweetsAdapter;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;
import com.walmartlabs.classwork.tweets.util.EndlessScrollListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    public static long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsAdapter(this, tweets);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        setupListView();
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        fetchAndPopulateTimeline(params);
    }

    private void setupListView() {
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

    private void fetchAndPopulateTimeline(RequestParams params) {
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
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
