package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.walmartlabs.classwork.tweets.adapters.ProfilePagerAdapter;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends BaseActivity {

    private TwitterClient client;
    private User user;
    private ViewPager viewPager;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        final String screenName = getIntent().getStringExtra("screen_name");
        client.getUserProfile(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);

                /*if (savedInstanceState == null) {
                    UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, userTimelineFragment);
                    ft.commit();
                }*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewprofilepager);
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), args));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.profiletabs);
        tabStrip.setViewPager(viewPager);

    }

    private void populateProfileHeader(User user) {
        // Populate data into the template view using the data object
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing= (TextView) findViewById(R.id.tvFollowing);
        TextView tvTagline= (TextView) findViewById(R.id.tvTagline);

        tvName.setText(user.getName());
        tvFollowers.setText(Integer.toString(user.getFollowersCount()));
        tvFollowing.setText(Integer.toString(user.getFriendsCount()));
        tvTagline.setText(user.getTagline());

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                            //.borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(Uri.parse(user.getProfileImageUrl()))
                /*.placeholder(R.drawable.ic_nocover)*/
                .transform(transformation)
                .into(ivProfileImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    public void showFollowers(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra("screen_name", user.getScreenName());
        intent.putExtra("type", "followers");
        startActivity(intent);
    }

    public void showFriends(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra("screen_name", user.getScreenName());
        intent.putExtra("type", "friends");
        startActivity(intent);
    }
}
