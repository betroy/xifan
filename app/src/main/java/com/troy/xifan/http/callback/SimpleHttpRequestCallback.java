package com.troy.xifan.http.callback;

import com.troy.xifan.http.exception.ApiException;

/**
 * Created by chenlongfei on 2017/1/7.
 */

public class SimpleHttpRequestCallback<T> implements HttpRequestCallback<T> {
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(T responseData) {

    }

    @Override
    public void onFail(ApiException apiException) {

    }
}
