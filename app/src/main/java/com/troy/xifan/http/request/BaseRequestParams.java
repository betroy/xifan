package com.troy.xifan.http.request;

import android.text.TextUtils;
import com.troy.xifan.config.Constants;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by chenlongfei on 2016/12/30.
 */

public class BaseRequestParams {
    private String id;
    private String mode;
    private String format;
    private String callback;
    private String page;
    private String count;

    public BaseRequestParams() {
        setFormat(Constants.RequestParams.FORMAT_HTML);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Map<String, String> convertToQueryMap() {
        Map<String, String> params = new HashMap<>();
        Class<?> cls = getClass();
        while (cls != null && !isSystemCalss(cls.getName())) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String key = field.getName();
                String value = getFieldValue(field);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                params.put(key, value);
            }
            cls = cls.getSuperclass();
        }
        return params;
    }

    public Map<String, RequestBody> convertToBodyMap() {
        Map<String, RequestBody> params = new HashMap<>();
        Class<?> cls = getClass();
        while (cls != null && !isSystemCalss(cls.getName())) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String key = field.getName();
                String value = getFieldValue(field);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                if (key.equals("photo")) {
                    File file = new File(value);
                    String fileKey = "photo\";filename=\"" + file.getName();
                    params.put(fileKey, createFilePartFormString(file));
                } else {
                    params.put(key, createPartFromString(value));
                }
            }
            cls = cls.getSuperclass();
        }
        return params;
    }

    private String getFieldValue(Field field) {
        try {
            field.setAccessible(true);
            if (field.get(this) instanceof String) {
                return (String) field.get(this);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

    private RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse(Constants.HttpBody.MULTIPART_FORM_DATA), value);
    }

    private RequestBody createFilePartFormString(File file) {
        return RequestBody.create(MediaType.parse(Constants.HttpBody.MULTIPART_FORM_DATA), file);
    }
}

