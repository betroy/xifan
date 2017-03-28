package com.troy.xifan.model.response;

import java.io.Serializable;

/**
 * Created by chenlongfei on 2017/3/24.
 */

public class NotificationRes implements Serializable {
    private String mentions;
    private String direct_messages;
    private String friend_requests;

    public String getMentions() {
        return mentions;
    }

    public void setMentions(String mentions) {
        this.mentions = mentions;
    }

    public String getDirect_messages() {
        return direct_messages;
    }

    public void setDirect_messages(String direct_messages) {
        this.direct_messages = direct_messages;
    }

    public String getFriend_requests() {
        return friend_requests;
    }

    public void setFriend_requests(String friend_requests) {
        this.friend_requests = friend_requests;
    }
}
