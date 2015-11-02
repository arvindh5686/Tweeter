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
import com.walmartlabs.classwork.tweets.models.User;

import java.util.List;

/**
 * Created by abalak5 on 10/21/15.
 */
public class UsersAdapter extends ArrayAdapter<User> {
    public BaseActivity activity;
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvScreenName;
        public TextView tvName;
    }

    public UsersAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
        this.activity = (BaseActivity) context;
    }

    // Translates a particular `Image` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f1f1f1"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#fafafa"));
        }

        // Populate data into the template view using the data object
        viewHolder.tvScreenName.setText(Html.fromHtml("@" + user.getScreenName()));
        viewHolder.tvName.setText(Html.fromHtml(user.getName()));

        try {
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                            //.borderWidthDp(0)
                    .cornerRadiusDp(10)
                    .oval(false)
                    .build();

            Picasso.with(getContext())
                    .load(Uri.parse(user.getProfileImageUrl()))
                /*.placeholder(R.drawable.ic_nocover)*/
                    .transform(transformation)
                    .into(viewHolder.ivProfileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
