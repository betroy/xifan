package com.troy.xifan.manage;

import com.troy.xifan.config.Constants;
import com.troy.xifan.model.request.User;
import com.troy.xifan.model.response.OAuthToken;
import com.troy.xifan.util.FileUtils;
import java.io.File;

/**
 * Created by chenlongfei on 2016/11/27.
 */

public class UserHolder {
    private static final String FILE_NAME = "userinfo.data";
    private static UserHolder sInstance;
    private File mCacheFile;

    public UserHolder() {
        mCacheFile = FileUtils.getCacheFile(FILE_NAME);
    }

    public static UserHolder getInstance() {
        if (sInstance == null) {
            synchronized (UserHolder.class) {
                if (sInstance == null) {
                    sInstance = new UserHolder();
                }
            }
        }
        return sInstance;
    }

    private void flushToken(User user) {
        OAuthToken oAuthToken = null;
        if (user != null) {
            oAuthToken = user.getToken();
        }
        if (oAuthToken != null) {
            Constants.FanFou.OAUTH_TOKEN = oAuthToken.getOauthToken();
            Constants.FanFou.OAUTH_TOKENSECRET = oAuthToken.getOauthTokenSecret();
        }
    }

    public void saveUser(User user) {
        flushToken(user);
        FileUtils.saveObjectToFile(user, mCacheFile);
    }

    public User readUser() {
        User user = (User) FileUtils.readObjectFromFile(mCacheFile);
        flushToken(user);
        return user;
    }

    public void cleanUser() {
        FileUtils.deleteFile(mCacheFile);
        Constants.FanFou.OAUTH_TOKEN = null;
        Constants.FanFou.OAUTH_TOKENSECRET = null;
    }
}