package com.walmartlabs.classwork.tweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by abalak5 on 10/21/15.
 */

@Table(name = "users")
public class User extends Model implements Parcelable {

    @Column(name = "name")
    private String name;
    @Column(name = "uid")
    private Long uid;
    @Column(name = "screenname")
    private String screenName;
    @Column(name = "profileimageurl")
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // Returns a Image given the expected JSON
    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return user;
    }

    // Record Finders
    public static User byId(long id) {
        return new Select().from(User.class).where("uid = ?", id).executeSingle();
    }

    public static List<User> recentItems() {
        return new Select().from(User.class).orderBy("id DESC").limit("300").execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeValue(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = (Long) in.readValue(Long.class.getClassLoader());
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

