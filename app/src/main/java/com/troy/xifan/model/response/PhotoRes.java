package com.troy.xifan.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlongfei on 2016/12/8.
 */

public class PhotoRes implements Parcelable {
    private String url;
    private String imageurl;
    private String thumburl;
    private String largeurl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getLargeurl() {
        return largeurl;
    }

    public void setLargeurl(String largeurl) {
        this.largeurl = largeurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.imageurl);
        dest.writeString(this.thumburl);
        dest.writeString(this.largeurl);
    }

    public PhotoRes() {
    }

    protected PhotoRes(Parcel in) {
        this.url = in.readString();
        this.imageurl = in.readString();
        this.thumburl = in.readString();
        this.largeurl = in.readString();
    }

    public static final Parcelable.Creator<PhotoRes> CREATOR = new Parcelable.Creator<PhotoRes>() {
        @Override
        public PhotoRes createFromParcel(Parcel source) {
            return new PhotoRes(source);
        }

        @Override
        public PhotoRes[] newArray(int size) {
            return new PhotoRes[size];
        }
    };
}
