package com.troy.xifan.model.response;

import java.io.Serializable;

/**
 * Created by chenlongfei on 2016/11/25.
 */

public class OAuthToken implements Serializable {
    private String oauthToken;
    private String oauthTokenSecret;

    public OAuthToken(String oauthToken, String oauthTokenSecret) {
        this.oauthToken = oauthToken;
        this.oauthTokenSecret = oauthTokenSecret;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }

    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }
}
