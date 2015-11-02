package com.walmartlabs.classwork.tweets.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.walmartlabs.classwork.tweets.activities.BaseActivity;
import com.walmartlabs.classwork.tweets.main.TwitterApplication;
import com.walmartlabs.classwork.tweets.models.Tweet;
import com.walmartlabs.classwork.tweets.net.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by abalak5 on 10/23/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    private EditText etMessage;
    private TwitterClient client;
    private int charactersCount = 0;
    private static boolean isReply = false;
    private static Tweet inReplyToTweet;

    public interface EditNameDialogListener {
        void onFinishEditDialog(Tweet tweet);
    }


    public ComposeTweetDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ComposeTweetDialog newInstance(Tweet tweet) {
        if (tweet != null) {
            isReply = true;
            inReplyToTweet = tweet;
        } else {
            isReply = false;
        }
        ComposeTweetDialog frag = new ComposeTweetDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        client = TwitterApplication.getRestClient();
        final View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        //hardcoding my profile image by default until verify_credentials api works

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(true)
                .build();

        ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        Picasso.with(getContext()).
                load(Uri.parse(getString(R.string.current_user_profile_image)))
                .transform(transformation)
                .into(ivProfileImage);

        final Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        final TextView tvCount = (TextView) view.findViewById(R.id.tvCount);
        final EditText etTweet = (EditText) view.findViewById(R.id.etTweet);
        etTweet.setText("");

        //if replying to a tweet
        if(isReply) {
            etTweet.setText("@" + inReplyToTweet.getUser().getScreenName() + " ");
            etTweet.setSelection(etTweet.getText().length());
        }
        final int currLength = 140 - etTweet.getText().toString().length();
        tvCount.setText(Integer.toString(currLength));

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(charactersCount == 0) btnTweet.setEnabled(true);
                charactersCount++;
                tvCount.setText(Integer.toString(currLength - charactersCount));
                Log.i("count", Integer.toString(charactersCount));
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etTweet = (EditText) view.findViewById(R.id.etTweet);
                final String status = etTweet.getEditableText().toString();
                Log.i("status", status);
                RequestParams params = new RequestParams();
                params.put("status", status);
                if (isReply) params.put("in_reply_to_status_id", inReplyToTweet.getUid());
                client.postTweet(params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dismiss();
                        Tweet postResponse = Tweet.fromJson(response);
                        postResponse.getUser().getName();
                        postResponse.getUser().getScreenName();
                        ((BaseActivity) getActivity()).onFinishEditDialog(postResponse);
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
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
