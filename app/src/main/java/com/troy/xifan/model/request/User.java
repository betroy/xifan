package com.troy.xifan.model.request;

import com.troy.xifan.model.response.OAuthToken;
import java.io.Serializable;

/**
 * Created by chenlongfei on 2016/11/24.
 */

public class User implements Serializable {
    private String userName;
    private String password;
    private OAuthToken token;

    public User(String userName, String password, OAuthToken token) {
        this.userName = userName;
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OAuthToken getToken() {
        return token;
    }

    public void setToken(OAuthToken token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
