package com.troy.xifan.http.response;

import com.troy.xifan.http.HttpTag;

/**
 * Created by chenlongfei on 2016/11/19.
 */

public class HttpResponseData<T> {
    private HttpTag tag;
    private int resultCode;
    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpTag getTag() {
        return tag;
    }

    public void setTag(HttpTag tag) {
        this.tag = tag;
    }
}
