package com.walmartlabs.classwork.tweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.tweets.R;
import com.walmartlabs.classwork.tweets.adapters.UsersAdapter;
import com.walmartlabs.classwork.tweets.fragments.UserListFragment;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import java.util.ArrayList;

public class UserListActivity extends BaseActivity {
    private TwitterClient client;
    private ArrayList<User> users;
    private UsersAdapter aUsers;
    private ListView lvUsers;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Intent intent = getIntent();
        String screenName = intent.getStringExtra("screen_name");
        String type = intent.getStringExtra("type");
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            UserListFragment userListFragment;
            userListFragment = UserListFragment.newInstance(screenName, type);

            ft.replace(R.id.flUserlist, userListFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
