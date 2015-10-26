package com.walmartlabs.classwork.tweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walmartlabs.classwork.tweets.activities.TimelineActivity;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by abalak5 on 10/23/15.
 */
public class EditNameDialog extends DialogFragment {

    private EditText etMessage;
    private TwitterClient client;

    public interface EditNameDialogListener {
        void onFinishEditDialog(Tweet tweet);
    }


    public EditNameDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditNameDialog newInstance(String title) {
        EditNameDialog frag = new EditNameDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        client = TwitterApplication.getRestClient();
        final View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etTweet = (EditText) view.findViewById(R.id.etTweet);
                final String status = etTweet.getEditableText().toString();
                Log.i("status", status);
                RequestParams params = new RequestParams();
                params.put("status", status);
                client.postTweet(params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dismiss();
                        Tweet postResponse = Tweet.fromJson(response);
                        TimelineActivity activity = (TimelineActivity) getActivity();
                        activity.onFinishEditDialog(postResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failure", errorResponse.toString());
                    }
                });
            }
        });

        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
