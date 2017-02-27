package com.troy.xifan.eventbus;

/**
 * Created by chenlongfei on 2017/1/9.
 */

public class SendStatusEvent {
    public final static String TYPE_SUCCESS = "success";
    public final static String TYPE_FAIL = "fail";

    private String type;

    public SendStatusEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
