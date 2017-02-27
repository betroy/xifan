package com.troy.xifan.http.callback;

import com.troy.xifan.eventbus.SendStatusEvent;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.model.response.StatusRes;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenlongfei on 2017/1/9.
 */

public class SendStatusRequestCallback implements HttpRequestCallback<StatusRes> {
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(StatusRes responseData) {
        EventBus.getDefault().post(new SendStatusEvent(SendStatusEvent.TYPE_SUCCESS));
    }

    @Override
    public void onFail(ApiException apiException) {
        EventBus.getDefault().post(new SendStatusEvent(SendStatusEvent.TYPE_FAIL));
    }
}
