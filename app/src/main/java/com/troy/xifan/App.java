package com.troy.xifan;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.chenenyu.router.Router;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.troy.xifan.model.response.UserRes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 2016/11/6.
 */

public class App extends Application {
    public static final String TAG = "Xifan";
    private static App sInstance;

    private UserRes user;

    private List<Activity> mActivityList = new ArrayList<>();

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        init();
    }

    private void init() {
        Router.initialize(this);

        Stetho.initializeWithDefaults(this);
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(1)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);            // default 0
    }

    public static App getInstance() {
        return sInstance;
    }

    public UserRes getUser() {
        return user;
    }

    public void setUser(UserRes user) {
        this.user = user;
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (!mActivityList.contains(activity)) {
                        mActivityList.add(activity);
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (mActivityList.contains(activity)) {
                        mActivityList.remove(activity);
                    }
                }
            };

    public List<Activity> getActivityList() {
        return mActivityList;
    }

    public void setActivityList(List<Activity> activityList) {
        mActivityList = activityList;
    }

    public void cleanActivityList() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }
}
