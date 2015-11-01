package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweets.R;
import com.walmartlabs.classwork.tweets.adapters.TweetsAdapter;
import com.walmartlabs.classwork.tweets.adapters.TweetsPagerAdapter;
import com.walmartlabs.classwork.tweets.fragments.ComposeTweetDialog;
import com.walmartlabs.classwork.tweets.fragments.TweetsListFragment;
import com.walmartlabs.classwork.tweets.models.Tweet;

public class TimelineActivity extends BaseActivity implements ComposeTweetDialog.EditNameDialogListener,
        TweetsAdapter.ProfileImageClickListener {


    private TweetsListFragment tweetsListFragment;
    private ComposeTweetDialog composeTweetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.setLoggingEnabled(true);
        setContentView(R.layout.activity_timeline);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4FAAF1")));
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
            composeTweetDialog = ComposeTweetDialog.newInstance(null);
            composeTweetDialog.show(fm, "fragment_compose_tweet");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
        /*tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();*/
    }

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
