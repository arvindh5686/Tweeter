package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by abalak5 on 10/31/15.
 */
public class BaseActivity extends AppCompatActivity {
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
}
