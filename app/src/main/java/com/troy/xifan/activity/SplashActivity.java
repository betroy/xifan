package com.troy.xifan.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import com.chenenyu.router.Router;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.manage.UserHolder;
import com.troy.xifan.model.request.User;

/**
 * Created by chenlongfei on 2016/11/7.
 */

public class SplashActivity extends BaseActivity {
    public static final long INTERVAL_TIME = 2 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(INTERVAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                checkUserInfo();
            }
        }.start();
    }

    private void checkUserInfo() {
        User user = UserHolder.getInstance().readUser();
        if (user != null) {
            Router.build(Constants.Router.MAIN).go(this);
        } else {
            Router.build(Constants.Router.LOGIN).go(this);
        }
        finish();
    }

    @Override
    public void initViews() {

    }
}
