package com.troy.xifan.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class StatusRes implements Parcelable {

    /**
     * created_at : Wed Dec 07 05:10:05 +0000 2016
     * id : rlDs4QRRpNg
     * rawid : 204208572
     * text : 转@番茄囝 那是ps的，这才是真的「@飯否小字报 转@王兴 男人的相貌风格差别真大。左边的海明威比右边的菲茨杰拉德还小三岁呢。」
     * source : 手机上网
     * truncated : false
     * in_reply_to_status_id :
     * in_reply_to_user_id :
     * favorited : false
     * in_reply_to_screen_name :
     * is_self : false
     * repost_status_id : hwYSUzDDT3s
     * repost_user_id : frankielove
     * repost_screen_name : 番茄囝
     * repost_status : {"created_at":"Wed Dec 07 02:46:55 +0000 2016","id":"hwYSUzDDT3s","rawid":204203870,"text":"那是ps的，这才是真的「@飯否小字报
     * 转@王兴 男人的相貌风格差别真大。左边的海明威比右边的菲茨杰拉德还小三岁呢。」","source":"","truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","favorited":false,"in_reply_to_screen_name":"","is_self":false,"location":"广东
     * 广州","user":{"id":"frankielove","name":"番茄囝","screen_name":"番茄囝","unique_id":"~05RpvQvujBo","location":"广东
     * 广州","gender":"男","birthday":"1991-02-16","description":"傻屌","profile_image_url":"http://s3.meituan.net/v1/mss_3d027b52ec5a4d589e68050845611e68/avatar/s0/00/82/15.jpg?1228661080","profile_image_url_large":"http://s3.meituan.net/v1/mss_3d027b52ec5a4d589e68050845611e68/avatar/l0/00/82/15.jpg?1228661080","url":"http://frankie-love.blog.163.com/","protected":false,"followers_count":108,"friends_count":60,"favourites_count":79,"statuses_count":4184,"photo_count":386,"following":false,"notifications":false,"created_at":"Wed
     * Jan 30 06:32:11 +0000 2008","utc_offset":28800,"profile_background_color":"#acdae5","profile_text_color":"#222222","profile_link_color":"#0066cc","profile_sidebar_fill_color":"#e2f2da","profile_sidebar_border_color":"#b2d1a3","profile_background_image_url":"http://static.fanfou.com/img/bg/0.png","profile_background_tile":false},"photo":{"url":"http://fanfou.com/photo/3A9ElDy7VEk","imageurl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@200w_200h_1l.jpg","thumburl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@120w_120h_1l.jpg","largeurl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@596w_1l.jpg"}}
     * location : 北京 海淀区
     * user : {"id":"wangxing","name":"王兴","screen_name":"王兴","unique_id":"~vlY6PYjjoLY","location":"北京
     * 海淀区","gender":"男","birthday":"0000-02-18","description":"如果我一整天都没看到、想到、或做过什么值得在饭否上说的事，那这一天就太浑浑噩噩了。
     * 美团创始人， 饭否创始人， 校内网创始人， 非典型清华工科男。 Create like a god. Command like a king. Work like a
     * slave.","profile_image_url":"http://s3.meituan.net/v1/mss_3d027b52ec5a4d589e68050845611e68/avatar/s0/00/31/n3.jpg?1179311049","profile_image_url_large":"http://s3.meituan.net/v1/mss_3d027b52ec5a4d589e68050845611e68/avatar/l0/00/31/n3.jpg?1179311049","url":"","protected":false,"followers_count":177555,"friends_count":741,"favourites_count":117,"statuses_count":9597,"photo_count":436,"following":true,"notifications":true,"created_at":"Sat
     * May 12 14:24:26 +0000 2007","utc_offset":28800,"profile_background_color":"#dadada","profile_text_color":"#403333","profile_link_color":"#990000","profile_sidebar_fill_color":"#cccaca","profile_sidebar_border_color":"#999191","profile_background_image_url":"http://static.fanfou.com/img/bg/3.jpg","profile_background_tile":true}
     * photo : {"url":"http://fanfou.com/photo/3A9ElDy7VEk","imageurl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@200w_200h_1l.jpg","thumburl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@120w_120h_1l.jpg","largeurl":"http://photo.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0d/c0/zg_483018.jpg@596w_1l.jpg"}
     */

    private String created_at;
    private String id;
    private int rawid;
    private String text;
    private String source;
    private boolean truncated;
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private boolean favorited;
    private String in_reply_to_screen_name;
    private boolean is_self;
    private String repost_status_id;
    private String repost_user_id;
    private String repost_screen_name;
    private StatusRes repost_status;
    private String location;
    private UserRes user;
    private PhotoRes photo;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRawid() {
        return rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public boolean isIs_self() {
        return is_self;
    }

    public void setIs_self(boolean is_self) {
        this.is_self = is_self;
    }

    public String getRepost_status_id() {
        return repost_status_id;
    }

    public void setRepost_status_id(String repost_status_id) {
        this.repost_status_id = repost_status_id;
    }

    public String getRepost_user_id() {
        return repost_user_id;
    }

    public void setRepost_user_id(String repost_user_id) {
        this.repost_user_id = repost_user_id;
    }

    public String getRepost_screen_name() {
        return repost_screen_name;
    }

    public void setRepost_screen_name(String repost_screen_name) {
        this.repost_screen_name = repost_screen_name;
    }

    public StatusRes getRepost_status() {
        return repost_status;
    }

    public void setRepost_status(StatusRes repost_status) {
        this.repost_status = repost_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserRes getUser() {
        return user;
    }

    public void setUser(UserRes user) {
        this.user = user;
    }

    public PhotoRes getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoRes photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.created_at);
        dest.writeString(this.id);
        dest.writeInt(this.rawid);
        dest.writeString(this.text);
        dest.writeString(this.source);
        dest.writeByte(this.truncated ? (byte) 1 : (byte) 0);
        dest.writeString(this.in_reply_to_status_id);
        dest.writeString(this.in_reply_to_user_id);
        dest.writeByte(this.favorited ? (byte) 1 : (byte) 0);
        dest.writeString(this.in_reply_to_screen_name);
        dest.writeByte(this.is_self ? (byte) 1 : (byte) 0);
        dest.writeString(this.repost_status_id);
        dest.writeString(this.repost_user_id);
        dest.writeString(this.repost_screen_name);
        dest.writeParcelable(this.repost_status, flags);
        dest.writeString(this.location);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.photo, flags);
    }

    public StatusRes() {
    }

    protected StatusRes(Parcel in) {
        this.created_at = in.readString();
        this.id = in.readString();
        this.rawid = in.readInt();
        this.text = in.readString();
        this.source = in.readString();
        this.truncated = in.readByte() != 0;
        this.in_reply_to_status_id = in.readString();
        this.in_reply_to_user_id = in.readString();
        this.favorited = in.readByte() != 0;
        this.in_reply_to_screen_name = in.readString();
        this.is_self = in.readByte() != 0;
        this.repost_status_id = in.readString();
        this.repost_user_id = in.readString();
        this.repost_screen_name = in.readString();
        this.repost_status = in.readParcelable(StatusRes.class.getClassLoader());
        this.location = in.readString();
        this.user = in.readParcelable(UserRes.class.getClassLoader());
        this.photo = in.readParcelable(PhotoRes.class.getClassLoader());
    }

    public static final Parcelable.Creator<StatusRes> CREATOR =
            new Parcelable.Creator<StatusRes>() {
                @Override
                public StatusRes createFromParcel(Parcel source) {
                    return new StatusRes(source);
                }

                @Override
                public StatusRes[] newArray(int size) {
                    return new StatusRes[size];
                }
            };
}
