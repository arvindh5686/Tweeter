package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tweets.R;
import com.walmartlabs.classwork.tweets.adapters.UsersAdapter;
import com.walmartlabs.classwork.tweets.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abalak5 on 11/1/15.
 */
public class UserListFragment extends Fragment{
    private UsersAdapter aUsers;
    private ArrayList<User> users;

    private SwipeRefreshLayout swipeContainer;
    private ListView lvUsers;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<User>();
        aUsers = new UsersAdapter(getActivity(), users);
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

    public void clear() {
        aUsers.clear();
    }
}
