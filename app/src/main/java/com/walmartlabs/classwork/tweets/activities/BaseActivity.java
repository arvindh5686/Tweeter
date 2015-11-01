package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by abalak5 on 10/31/15.
 */
public class BaseActivity extends AppCompatActivity {
    public void onProfileView(String screenName) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("screen_name", screenName);
        startActivity(intent);
    }
}
