package com.troy.xifan.util;

import android.text.TextUtils;
import com.orhanobut.logger.Logger;
import com.troy.xifan.config.Constants;
import com.troy.xifan.model.response.OAuthToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by chenlongfei on 2016/11/18.
 */

public final class XAuthUtils {
    public static final String OAUTH = "OAuth ";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String CHARSET = "UTF-8";
    private static final String TOKEN_REGEX = "oauth_token=([^&]+)";
    private static final String TOKEN_SECRET_REGEX = "oauth_token_secret=([^&]+)";

    private XAuthUtils() {

    }

    public static String getAuthorization(Request request) {
        Map<String, String> oAuthParams = makeOAuthParams(request);
        Set<Map.Entry<String, String>> entrySet = oAuthParams.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append(OAUTH);
        for (Map.Entry<String, String> entry : entrySet) {
            if (sb.length() > OAUTH.length()) {
                sb.append(", ");
            }
            sb.append(String.format("%s=\"%s\"", entry.getKey(),
                    OAuthEncoder.encode(entry.getValue())));
        }
        String authorization = sb.toString();
        Logger.i("authorization:" + authorization);
        return authorization;
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static String getNonce() {
        String nonce = Base64.encodeBytes(UUID.randomUUID().toString().getBytes());
        return nonce;
    }

    public static String getSignature(Map<String, String> oAuthParams, Request request) {
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> params = new TreeMap<>();
        StringBuilder sb = new StringBuilder();
        HttpUrl httpUrl = request.url();
        int querySize = request.url().querySize();
        for (int i = 0; i < querySize; i++) {
            queryParams.put(httpUrl.queryParameterName(i),
                    OAuthEncoder.encode(httpUrl.queryParameterValue(i)));
        }
        params.putAll(oAuthParams);
        params.putAll(queryParams);
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            sb.append("&");
            sb.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        String queryItems = sb.toString().substring(1);
        String baseString = String.format("%s&%s&%s", request.method(),
                OAuthEncoder.encode("http://" + httpUrl.url().getHost() + httpUrl.url().getPath()),
                OAuthEncoder.encode(queryItems));
        String signString = doSign(baseString);
        Logger.i("baseString:%s\nsignString:%s", baseString, signString);
        return signString;
    }

    public static OAuthToken parseToken(String responseData) {
        String oauthToken = PatternUtils.extractToken(TOKEN_REGEX, responseData);
        String oauthTokenSecret = PatternUtils.extractToken(TOKEN_SECRET_REGEX, responseData);
        Logger.i("oauthToken:%s,oauthTokenSecret%s", oauthToken, oauthTokenSecret);
        return new OAuthToken(oauthToken, oauthTokenSecret);
    }

    private static String doSign(String baseString) {
        String apiSecret = Constants.FanFou.CONSUMER_SECRET;
        String tokenSecret = Constants.FanFou.OAUTH_TOKENSECRET;
        String keyString = OAuthEncoder.encode(apiSecret) + '&';
        if (tokenSecret != null) {
            keyString += OAuthEncoder.encode(tokenSecret);
        }

        try {
            SecretKeySpec key = new SecretKeySpec(keyString.getBytes(CHARSET), HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(key);
            byte[] bytes = mac.doFinal(baseString.getBytes(CHARSET));
            return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("doSign error:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String bytesToBase64String(byte[] bytes) {
        return Base64.encodeBytes(bytes);
    }

    private static Map<String, String> makeOAuthParams(Request request) {
        Map<String, String> oAuthParams = new TreeMap<>();
        String oauthToken = Constants.FanFou.OAUTH_TOKEN;
        String username = Constants.FanFou.USERNAME;
        String password = Constants.FanFou.PASSWORD;

        oAuthParams.put(Constants.XAuth.X_AUTH_MODE, Constants.FanFou.X_AUTH_MODE_VALUE);
        oAuthParams.put(Constants.XAuth.OAUTH_CONSUMER_KEY, Constants.FanFou.CONSUMER_KEY);
        oAuthParams.put(Constants.XAuth.OAUTH_VERSION, Constants.FanFou.OAUTH_VERSION_VALUE);
        oAuthParams.put(Constants.XAuth.OAUTH_SIGNATURE_METHOD,
                Constants.FanFou.OAUTH_SIGNATURE_METHOD_VALUE);
        oAuthParams.put(Constants.XAuth.OAUTH_TIMESTAMP, getTimestamp());
        oAuthParams.put(Constants.XAuth.OAUTH_NONCE, getNonce());

        if (!TextUtils.isEmpty(oauthToken)) {
            oAuthParams.put(Constants.XAuth.OAUTH_TOKEN, oauthToken);
        } else {
            if (!TextUtils.isEmpty(username)) {
                oAuthParams.put(Constants.XAuth.X_AUTH_USERNAME, username);
            }
            if (!TextUtils.isEmpty(password)) {
                oAuthParams.put(Constants.XAuth.X_AUTH_PASSWORD, password);
            }
        }
        oAuthParams.put(Constants.XAuth.OAUTH_SIGNATURE, getSignature(oAuthParams, request));
        return oAuthParams;
    }
}
