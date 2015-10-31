package com.walmartlabs.classwork.tweets.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.walmartlabs.classwork.tweets.fragments.UserTimelineFragment;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();

        client.getCurrentUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }
        });

        String screenName = getIntent().getStringExtra("screen_name");

        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        // Populate data into the template view using the data object
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing= (TextView) findViewById(R.id.tvFollowing);
        TextView tvTagline= (TextView) findViewById(R.id.tvTagline);

        tvName.setText(user.getName());
        tvFollowers.setText(Integer.toString(user.getFollowersCount()) + " Followers");
        tvFollowing.setText(Integer.toString(user.getFriendsCount()) + " Following");
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
}
