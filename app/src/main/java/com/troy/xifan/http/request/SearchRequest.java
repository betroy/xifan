package com.troy.xifan.http.request;

/**
 * Created by chenlongfei on 2017/1/11.
 */

public class SearchRequest extends BaseRequestParams {
    private String q;
    private String since_id;
    private String max_id;
    private String count;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSince_id() {
        return since_id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    @Override
    public String getCount() {
        return count;
    }

    @Override
    public void setCount(String count) {
        this.count = count;
    }
}
