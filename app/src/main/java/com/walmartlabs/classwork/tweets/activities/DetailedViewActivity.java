package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.walmartlabs.classwork.tweets.fragments.ComposeTweetDialog;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

public class DetailedViewActivity extends BaseActivity implements ComposeTweetDialog.EditNameDialogListener {

    private ComposeTweetDialog composeTweetDialog;
    private Tweet tweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setContentView(R.layout.activity_detailed_view);
        Intent i = getIntent();
        tweet = i.getParcelableExtra("tweet");
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                        //.borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(Uri.parse(tweet.getUser().getProfileImageUrl()))
                .transform(transformation)
                .into(ivProfileImage);
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4FAAF1")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void replyToTweet(View view) {
        FragmentManager fm = getSupportFragmentManager();
        composeTweetDialog = ComposeTweetDialog.newInstance(tweet);
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
    }

    public void retweet(final View view) {
        client.postReTweet(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                changeRetweetIcon(view);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
            }
        });
    }

    public void changeRetweetIcon(View view) {
        Picasso.with(this)
                .load(R.mipmap.ic_retweeted)
                .into((ImageView) view);
        view.setEnabled(false);
    }
}
