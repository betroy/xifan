package com.troy.xifan.model.response;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/10.
 */

public class TrendsRes implements Parcelable {
    /**
     * as_of : Thu Nov 10 09:57:23 +0000 2011
     * trends : [{"name":"萤火一号","query":"萤火一号|火星|变轨","url":"http://fanfou.com/q/%E8%90%A4%E7%81%AB%E4%B8%80%E5%8F%B7%7C%E7%81%AB%E6%98%9F%7C%E5%8F%98%E8%BD%A8"},{"name":"土耳其地震","query":"土耳其|地震","url":"http://fanfou.com/q/%E5%9C%9F%E8%80%B3%E5%85%B6%7C%E5%9C%B0%E9%9C%87"},{"name":"《失恋33天》","query":"33天|白百何","url":"http://fanfou.com/q/33%E5%A4%A9%7C%E7%99%BD%E7%99%BE%E4%BD%95"},{"name":"股市大跌","query":"股市|国债","url":"http://fanfou.com/q/%E8%82%A1%E5%B8%82%7C%E5%9B%BD%E5%80%BA"},{"name":"光棍节","query":"光棍|神棍|六一","url":"http://fanfou.com/q/%E5%85%89%E6%A3%8D%7C%E7%A5%9E%E6%A3%8D%7C%E5%85%AD%E4%B8%80"},{"name":"北方降温","query":"降温|冷空气","url":"http://fanfou.com/q/%E9%99%8D%E6%B8%A9%7C%E5%86%B7%E7%A9%BA%E6%B0%94"}]
     */
    private String as_of;
    private List<Trends> trends;

    public TrendsRes() {
    }

    public String getAs_of() {
        return as_of;
    }

    public void setAs_of(String as_of) {
        this.as_of = as_of;
    }

    public List<Trends> getTrends() {
        return trends;
    }

    public void setTrends(List<Trends> trends) {
        this.trends = trends;
    }

    public static class Trends {
        /**
         * name : 萤火一号
         * query : 萤火一号|火星|变轨
         * url : http://fanfou.com/q/%E8%90%A4%E7%81%AB%E4%B8%80%E5%8F%B7%7C%E7%81%AB%E6%98%9F%7C%E5%8F%98%E8%BD%A8
         */

        private String name;
        private String query;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.as_of);
        dest.writeList(this.trends);
    }

    protected TrendsRes(Parcel in) {
        this.as_of = in.readString();
        this.trends = new ArrayList<Trends>();
        in.readList(this.trends, Trends.class.getClassLoader());
    }

    public static final Creator<TrendsRes> CREATOR = new Creator<TrendsRes>() {
        @Override
        public TrendsRes createFromParcel(Parcel source) {
            return new TrendsRes(source);
        }

        @Override
        public TrendsRes[] newArray(int size) {
            return new TrendsRes[size];
        }
    };
}
