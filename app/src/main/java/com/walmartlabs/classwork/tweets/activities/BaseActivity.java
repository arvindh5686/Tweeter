package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.walmartlabs.classwork.tweets.fragments.ComposeTweetDialog;
import com.walmartlabs.classwork.tweets.models.Tweet;

/**
 * Created by abalak5 on 10/31/15.
 */
public class BaseActivity extends AppCompatActivity implements TweetActions {

    private ComposeTweetDialog composeTweetDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4FAAF1")));
    }

    public void onProfileView(String screenName) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("screen_name", screenName);
        startActivity(intent);
    }

    @Override
    public void replyToTweet(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        composeTweetDialog = ComposeTweetDialog.newInstance(tweet);
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
    }

    @Override
    public void reTweet(Tweet tweet) {
        //  ((TimelineActivity) this.activity).retweet(tweet);
    }
}
