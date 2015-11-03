package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmartlabs.classwork.tweets.adapters.UsersAdapter;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.User;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abalak5 on 11/1/15.
 */
public class UserListFragment extends Fragment{
    private UsersAdapter aUsers;
    private ArrayList<User> users;
    private TwitterClient client;

    private SwipeRefreshLayout swipeContainer;
    private ListView lvUsers;
    private static final String FOLLOWERS = "Followers";
    private static final String FRIENDS = "Friends";

    public static UserListFragment newInstance(String screenName, String userType) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        args.putString("user_type", userType);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<User>();
        aUsers = new UsersAdapter(getActivity(), users);
        client = TwitterApplication.getRestClient();
        fetchAndPopulateUserList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        lvUsers = (ListView) view.findViewById(R.id.lvUsers);
        lvUsers.setAdapter(aUsers);
        return view;
    }

    public void addAll(List<User> users) {
        aUsers.addAll(users);
    }

    private void fetchAndPopulateUserList() {
        String screenName = getArguments().getString("screen_name");
        String userType = getArguments().getString("user_type");

        if(userType.equalsIgnoreCase(FOLLOWERS)) {
            client.getFollowersList(screenName, getHandler(FOLLOWERS));
        } else {
            client.getFriendsList(screenName, getHandler(FRIENDS));
        }
    }

    public JsonHttpResponseHandler getHandler(final String userType) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<User> users = User.fromJsonArray(response.getJSONArray("users"));
                    addAll(users);
                    getActivity().setTitle(userType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        };
    }
}
