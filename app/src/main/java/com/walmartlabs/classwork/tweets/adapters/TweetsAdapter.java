package com.walmartlabs.classwork.tweets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweets.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.walmartlabs.classwork.tweets.activities.BaseActivity;
import com.walmartlabs.classwork.tweets.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by abalak5 on 10/21/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public BaseActivity activity;
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvScreenName;
        public TextView tvName;
        public TextView tvBody;
        public TextView tvRelativeTimeStamp;
    }

    public TweetsAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
        this.activity = (BaseActivity) context;
    }

    // Translates a particular `Image` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final Tweet tweet = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvRelativeTimeStamp = (TextView)convertView.findViewById(R.id.tvRelativeTimeStamp);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f1f1f1"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#fafafa"));
        }

        final String screenName = tweet.getUser().getScreenName();
        // Populate data into the template view using the data object
        viewHolder.tvScreenName.setText(Html.fromHtml("@" + tweet.getUser().getScreenName()));
        viewHolder.tvName.setText(Html.fromHtml(tweet.getUser().getName()));
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onProfileView(screenName);
            }
        });

        ImageView ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.replyToTweet(tweet);
            }
        });

        ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.reTweet(tweet);
            }
        });
        try {
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            String currDateStr = df.format(Calendar.getInstance().getTime());
            Date currDate = df.parse(currDateStr);

            Date tweetDate = getTimeStamp(tweet.getCreatedAt());
            String timeStamp = getRelativeTimeStamp(currDate, tweetDate);
            viewHolder.tvRelativeTimeStamp.setText(timeStamp);
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    //.borderWidthDp(0)
                    .cornerRadiusDp(10)
                    .oval(false)
                    .build();

            Picasso.with(getContext())
                    .load(Uri.parse(tweet.getUser().getProfileImageUrl()))
                /*.placeholder(R.drawable.ic_nocover)*/
                    .transform(transformation)
                    .into(viewHolder.ivProfileImage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen
        return convertView;
    }



    public static Date getTimeStamp(String date) throws ParseException {
        final String TWITTER="EEE MMM dd HH:mm:ss z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        sf.setTimeZone(TimeZone.getTimeZone("GMT-04:00"));
        sf.setLenient(true);
        return sf.parse(date);
    }

    public static String getRelativeTimeStamp(Date currDate, Date tweetDate) {

        String timeStamp = "";
        long difference = currDate.getTime() - tweetDate.getTime();

        long seconds = 1000;
        long minutes = seconds * 60;
        long hours = minutes * 60;
        long days = hours * 24;

        long elapsedDays = difference / days ;
        timeStamp += elapsedDays > 0 && timeStamp.equalsIgnoreCase("") ? Long.toString(elapsedDays) + "d" : "";
        difference = difference % days;

        long elapsedHours = difference / hours;
        timeStamp += elapsedHours > 0 && timeStamp.equalsIgnoreCase("") ? Long.toString(elapsedHours) + "h" : "";
        difference = difference % hours;

        long elapsedMinutes = difference / minutes;
        timeStamp += elapsedMinutes > 0 && timeStamp.equalsIgnoreCase("") ? Long.toString(elapsedMinutes) + "m" : "";
        difference = difference % minutes;

        long elapsedSeconds = difference / seconds;
        timeStamp += elapsedSeconds > 0 && timeStamp.equalsIgnoreCase("") ? Long.toString(elapsedSeconds) + "s" : "";

        return timeStamp;
    }
}
