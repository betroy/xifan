package com.troy.xifan.model.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class UserRes implements Parcelable {

    /**
     * id : test
     * name : 测试昵称
     * screen_name : 测试昵称
     * location : 北京 海淀区
     * gender : 男
     * birthday : 2105-03-11
     * description : 测试帐号
     * profile_image_url : http://avatar3.fanfou.com/s0/00/5n/sk.jpg?1320913295
     * profile_image_url_large : http://avatar3.fanfou.com/l0/00/5n/sk.jpg?1320913295
     * url : http://fanfou.com/test
     * protected : true
     * followers_count : 9
     * friends_count : 16
     * favourites_count : 23
     * statuses_count : 124
     * following : false
     * notifications : false
     * created_at : Sat Jun 09 23:56:33 +0000 2007
     * utc_offset : 28800
     * profile_background_color : #ffffe5
     * profile_text_color : #004040
     * profile_link_color : #ff0000
     * profile_sidebar_fill_color : #ffefbf
     * profile_sidebar_border_color : #ffac80
     * profile_background_image_url : http://avatar.fanfou.com/b0/00/5n/sk_1320749993.jpg
     * profile_background_tile : true
     * status : {"created_at":"Thu Nov 10 09:37:34 +0000 2011","id":"XRFWGErKgGI","text":"这是神马？","source":"<a
     * href=\"http://abc.fanfouapps.com\" target=\"_blank\">ABC<\/a>","truncated":false,"in_reply_to_lastmsg_id":"","in_reply_to_user_id":"","favorited":false,"in_reply_to_screen_name":""}
     */

    private String id;
    private String name;
    private String screen_name;
    private String location;
    private String gender;
    private String birthday;
    private String description;
    private String profile_image_url;
    private String profile_image_url_large;
    private String url;
    @SerializedName("protected") private boolean protectedX;
    private int followers_count;
    private int friends_count;
    private int favourites_count;
    private int statuses_count;
    private boolean following;
    private boolean notifications;
    private String created_at;
    private int utc_offset;
    private String profile_background_color;
    private String profile_text_color;
    private String profile_link_color;
    private String profile_sidebar_fill_color;
    private String profile_sidebar_border_color;
    private String profile_background_image_url;
    private boolean profile_background_tile;
    private StatusRes status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_large() {
        return profile_image_url_large;
    }

    public void setProfile_image_url_large(String profile_image_url_large) {
        this.profile_image_url_large = profile_image_url_large;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isProtectedX() {
        return protectedX;
    }

    public void setProtectedX(boolean protectedX) {
        this.protectedX = protectedX;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(int utc_offset) {
        this.utc_offset = utc_offset;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public void setProfile_background_color(String profile_background_color) {
        this.profile_background_color = profile_background_color;
    }

    public String getProfile_text_color() {
        return profile_text_color;
    }

    public void setProfile_text_color(String profile_text_color) {
        this.profile_text_color = profile_text_color;
    }

    public String getProfile_link_color() {
        return profile_link_color;
    }

    public void setProfile_link_color(String profile_link_color) {
        this.profile_link_color = profile_link_color;
    }

    public String getProfile_sidebar_fill_color() {
        return profile_sidebar_fill_color;
    }

    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color) {
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }

    public String getProfile_sidebar_border_color() {
        return profile_sidebar_border_color;
    }

    public void setProfile_sidebar_border_color(String profile_sidebar_border_color) {
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public boolean isProfile_background_tile() {
        return profile_background_tile;
    }

    public void setProfile_background_tile(boolean profile_background_tile) {
        this.profile_background_tile = profile_background_tile;
    }

    public StatusRes getStatus() {
        return status;
    }

    public void setStatus(StatusRes status) {
        this.status = status;
    }

    public static class StatusEntity {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.screen_name);
        dest.writeString(this.location);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.description);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.profile_image_url_large);
        dest.writeString(this.url);
        dest.writeByte(this.protectedX ? (byte) 1 : (byte) 0);
        dest.writeInt(this.followers_count);
        dest.writeInt(this.friends_count);
        dest.writeInt(this.favourites_count);
        dest.writeInt(this.statuses_count);
        dest.writeByte(this.following ? (byte) 1 : (byte) 0);
        dest.writeByte(this.notifications ? (byte) 1 : (byte) 0);
        dest.writeString(this.created_at);
        dest.writeInt(this.utc_offset);
        dest.writeString(this.profile_background_color);
        dest.writeString(this.profile_text_color);
        dest.writeString(this.profile_link_color);
        dest.writeString(this.profile_sidebar_fill_color);
        dest.writeString(this.profile_sidebar_border_color);
        dest.writeString(this.profile_background_image_url);
        dest.writeByte(this.profile_background_tile ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.status, flags);
    }

    public UserRes() {
    }

    protected UserRes(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.screen_name = in.readString();
        this.location = in.readString();
        this.gender = in.readString();
        this.birthday = in.readString();
        this.description = in.readString();
        this.profile_image_url = in.readString();
        this.profile_image_url_large = in.readString();
        this.url = in.readString();
        this.protectedX = in.readByte() != 0;
        this.followers_count = in.readInt();
        this.friends_count = in.readInt();
        this.favourites_count = in.readInt();
        this.statuses_count = in.readInt();
        this.following = in.readByte() != 0;
        this.notifications = in.readByte() != 0;
        this.created_at = in.readString();
        this.utc_offset = in.readInt();
        this.profile_background_color = in.readString();
        this.profile_text_color = in.readString();
        this.profile_link_color = in.readString();
        this.profile_sidebar_fill_color = in.readString();
        this.profile_sidebar_border_color = in.readString();
        this.profile_background_image_url = in.readString();
        this.profile_background_tile = in.readByte() != 0;
        this.status = in.readParcelable(StatusRes.class.getClassLoader());
    }

    public static final Creator<UserRes> CREATOR = new Creator<UserRes>() {
        @Override
        public UserRes createFromParcel(Parcel source) {
            return new UserRes(source);
        }

        @Override
        public UserRes[] newArray(int size) {
            return new UserRes[size];
        }
    };
}
