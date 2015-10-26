package com.walmartlabs.classwork.tweets.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.walmartlabs.classwork.tweets.activities.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abalak5 on 10/21/15.
 */

@Table(name = "tweets")
public class Tweet extends Model implements Parcelable {
    //unique id for tweet
    @Column(name = "uid")
    private long uid;
    @Column(name = "body")
    private String body;
    @Column(name = "user")
    private User user;
    @Column(name = "createdAt")
    private String createdAt;

    public static String tweetMessage;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        //get last tweets uid and subtract one as max_id is inclusive
        TimelineActivity.maxId = tweets.get(tweets.size() - 1).getUid() - 1;
        //get first tweets uid and use as is as since_id is not inclusive
        TimelineActivity.sinceId = tweets.get(0).getUid();

        return tweets;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            Log.i("uid", Long.toString(tweet.uid));
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return tweet;
    }

    // Record Finders
    public static Tweet byId(long id) {
        return new Select().from(Tweet.class).where("uid = ?", id).executeSingle();
    }

    public static List<Tweet> recentItems() {
        return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.body);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.uid = in.readLong();
        this.body = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
