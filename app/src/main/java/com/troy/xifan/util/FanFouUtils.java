package com.troy.xifan.util;

import android.text.Html;
import com.troy.xifan.model.response.StatusRes;

/**
 * Created by chenlongfei on 2017/1/4.
 */

public class FanFouUtils {
    public static String formatAt(StatusRes statusRes) {
        return formatAt(statusRes.getUser().getName());
    }

    public static String formatAt(String userName) {
        return String.format("@%s ", userName);
    }

    public static String formatRepeat(StatusRes statusRes) {
        return String.format("转@%s %s", statusRes.getUser().getName(),
                Html.fromHtml(statusRes.getText()));
    }
}
