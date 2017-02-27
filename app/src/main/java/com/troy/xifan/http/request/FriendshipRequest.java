package com.troy.xifan.http.request;

/**
 * Created by chenlongfei on 2017/2/16.
 */

public class FriendshipRequest extends BaseRequestParams {
    private String user_a;
    private String user_b;

    public String getUser_a() {
        return user_a;
    }

    public void setUser_a(String user_a) {
        this.user_a = user_a;
    }

    public String getUser_b() {
        return user_b;
    }

    public void setUser_b(String user_b) {
        this.user_b = user_b;
    }
}
