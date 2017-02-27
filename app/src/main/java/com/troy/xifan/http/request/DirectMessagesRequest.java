package com.troy.xifan.http.request;

/**
 * Created by chenlongfei on 2017/2/21.
 */

public class DirectMessagesRequest extends BaseRequestParams {
    private String user;
    private String text;
    private String in_reply_to_id;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIn_reply_to_id() {
        return in_reply_to_id;
    }

    public void setIn_reply_to_id(String in_reply_to_id) {
        this.in_reply_to_id = in_reply_to_id;
    }
}
