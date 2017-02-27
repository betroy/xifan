package com.troy.xifan.http.callback;

import com.troy.xifan.http.exception.ApiException;

/**
 * Created by chenlongfei on 2016/11/19.
 */

public interface HttpRequestCallback<T> {
    void onStart();

    void onSuccess(T responseData);

    void onFail(ApiException apiException);
}
