package com.troy.xifan.util;

import com.troy.xifan.BuildConfig;
import com.troy.xifan.model.response.StatusRes;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by chenlongfei on 2016/12/10.
 */

public final class Utils {
    private static String FANFOU_DATE_FORMAT_STRING = "EEE MMM dd HH:mm:ss Z yyyy";
    private static SimpleDateFormat sFanFouDateFormat =
            new SimpleDateFormat(FANFOU_DATE_FORMAT_STRING, Locale.US);
    private static String SIMPLE_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sSimpleDateFormat =
            new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING, Locale.US);
    private static String DM_DATE_FORMAT_STRING = "yyyy年MM月dd日";
    private static SimpleDateFormat sDMDateFormat =
            new SimpleDateFormat(DM_DATE_FORMAT_STRING, Locale.US);
    private static final ParsePosition sPosition = new ParsePosition(0);

    public static final long MINUTE = 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;

    //格式化时间
    public static Date stringToDate(String strDate) {
        sPosition.setIndex(0);
        return sFanFouDateFormat.parse(strDate, sPosition);
    }

    public static String getDMDateFormatString(String strDate) {
        return sDMDateFormat.format(stringToDate(strDate));
    }

    public static String formatFanFouDate(Date date) {
        return sFanFouDateFormat.format(date);
    }

    //时间间隔
    public static String getIntervalTime(String strDate) {
        Date date = stringToDate(strDate);
        long lastTime = date.getTime();
        long currentTime = System.currentTimeMillis();
        long intervalSeconds = (currentTime - lastTime) / 1000;
        if (intervalSeconds < MINUTE) {
            return "刚刚";
        } else if (intervalSeconds < HOUR) {
            return intervalSeconds / MINUTE + "分钟前";
        } else if (intervalSeconds < DAY) {
            return intervalSeconds / HOUR + "小时前";
        } else {
            return intervalSeconds / DAY + "天前";
        }
    }

    //获取最后一条消息的ID
    public static String getMaxId(List<StatusRes> responseData) {
        String id = "";
        if (responseData.size() > 0) {
            id = responseData.get(responseData.size() - 1).getId();
        }
        return id;
    }

    //获取最新一条消息的ID
    public static String getSinceId(List<StatusRes> responseData) {
        String id = null;
        if (responseData.size() > 0) {
            id = responseData.get(0).getId();
        }
        return id;
    }

    //获取版本号
    public static String getVersionName() {
        return "v" + BuildConfig.VERSION_NAME;
    }
}
