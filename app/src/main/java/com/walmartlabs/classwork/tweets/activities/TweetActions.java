package com.walmartlabs.classwork.tweets.activities;

import com.walmartlabs.classwork.tweets.models.Tweet;

/**
 * Created by abalak5 on 11/2/15.
 */
public interface TweetActions {
    public void replyToTweet(Tweet tweet);
    public void reTweet(Tweet tweet);
    public void onFinishEditDialog(Tweet tweet);
}
