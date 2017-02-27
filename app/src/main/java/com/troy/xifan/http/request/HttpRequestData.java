package com.troy.xifan.http.request;

import com.troy.xifan.config.Constants;
import com.troy.xifan.http.Headers;
import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;
/**
 * Created by chenlongfei on 2016/11/10.
 */

public class HttpRequestData {
    private Headers headers;
    private String url;
    private String httpMethod;
    private Map<String, RequestBody> bodyParams;
    private HttpQueryParams queryParams;

    public HttpQueryParams getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(HttpQueryParams queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeadersMap();
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, RequestBody> getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(Map<String, RequestBody> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public HttpRequestData(Headers headers, String url, String httpMethod,
            Map<String, RequestBody> bodyParams, HttpQueryParams queryParams) {
        this.headers = headers;
        this.url = url;
        this.httpMethod = httpMethod;
        this.bodyParams = bodyParams;
        this.queryParams = queryParams;
    }

    public static class Builder {
        private Headers headers;
        private String url;
        private String httpMethod;
        private Map<String, RequestBody> bodyParams;
        private HttpQueryParams queryParams;

        public Builder setHeaders(Headers headers) {
            this.headers = headers;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder setBodyParam(String key, String value) {
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            return this;
        }

        public HttpQueryParams getQueryParams() {
            return queryParams;
        }

        public Builder setQueryParams(HttpQueryParams queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public HttpRequestData build() {
            makeDefault();
            return new HttpRequestData(headers, url, httpMethod, bodyParams, queryParams);
        }

        private void makeDefault() {
            if (queryParams == null) {
                queryParams = new HttpQueryParams();
            }

            if (headers == null) {
                headers = new Headers.Builder().build();
            }

            if (httpMethod == null) {
                httpMethod = Constants.HttpMethod.GET;
            }
        }

    }
}
