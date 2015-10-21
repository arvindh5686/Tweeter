package com.codepath.apps.tweets;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abalak5 on 10/21/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
    }

    public TweetsAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    // Translates a particular `Image` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Tweet tweet = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate data into the template view using the data object
        viewHolder.tvUserName.setText(Html.fromHtml(tweet.getUser().getName()));
        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));
        Picasso.with(getContext()).load(Uri.parse(tweet.getUser().getProfileImageUrl()))/*.placeholder(R.drawable.ic_nocover)*/.into(viewHolder.ivProfileImage);
        // Return the completed view to render on screen
        return convertView;
    }
}
