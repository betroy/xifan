package com.troy.xifan.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.troy.xifan.App;
import com.troy.xifan.config.Constants;

/**
 * Created by chenlongfei on 2017/2/25.
 */

public class SharedPreUtils {
    private SharedPreUtils() {

    }

    public static String getString(String key) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(key, "");
    }

    public static void pusString(String key, String value) {
        SharedPreferences preferences = getSharedPreferences();
        preferences.edit().putString(key, value).apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return App.getInstance()
                .getSharedPreferences(Constants.SharedPreferences.NAME, Context.MODE_PRIVATE);
    }
}
