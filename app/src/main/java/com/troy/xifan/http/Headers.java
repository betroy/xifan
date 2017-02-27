package com.troy.xifan.http;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlongfei on 2016/11/18.
 */

public class Headers {
    private Map<String, String> headers;

    public Headers(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeadersMap() {
        return headers;
    }

    public static class Builder {
        private static final String USER_AGENT = "User-Agent";
        private static final String DEFAULT_USER_AGENT = System.getProperty("http.agent");

        private static final Map<String, String> DEFAULT_HEADERS;

        private Map<String, String> headers;

        static {
            Map<String, String> headers = new HashMap<>(1);
            headers.put(USER_AGENT, DEFAULT_USER_AGENT);
            DEFAULT_HEADERS = Collections.unmodifiableMap(headers);
        }

        public Builder setHeader(String key, String values) {
            if (headers == null) {
                headers = new HashMap<>();
            }

            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(values)) {
                headers.put(key, values);
            }
            return this;
        }

        public Headers build() {
            makeDefault();
            return new Headers(headers);
        }

        private void makeDefault() {
            if (headers == null) {
                headers = new HashMap<>();
            }

            headers.putAll(DEFAULT_HEADERS);
        }
    }
}
