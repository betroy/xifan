package com.troy.xifan.http.exception;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.troy.xifan.App;
import com.troy.xifan.R;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by chenlongfei on 2016/11/27.
 */

public class ExceptionHandle {
    //HTTP状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int RANGE_NOT_SATISFIABLE = 416;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        e.printStackTrace();
        Logger.e("http request error result:\n" + e.fillInStackTrace());
        Context context = App.getInstance().getApplicationContext();

        if (e instanceof HttpException) {
            ApiException apiException = null;
            HttpException httpException = (HttpException) e;
            String errorMessage = null;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    errorMessage = context.getString(R.string.http_unauthorized_error);
                    break;
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case RANGE_NOT_SATISFIABLE:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                case GATEWAY_TIMEOUT:
                    errorMessage = context.getString(R.string.http_service_error);
                    break;
            }

            try {
                String errorBody = httpException.response().errorBody().string();
                if (!TextUtils.isEmpty(errorBody)) {
                    errorMessage = errorBody;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            apiException = new ApiException(ErrorCode.ERROR_UNAUTHORIZED, errorMessage);
            return apiException;
        }

        if (e instanceof ConnectException) {
            return new ApiException(ErrorCode.ERROR_NO_CONNECT,
                    context.getString(R.string.http_connect_error));
        }

        if (e instanceof JsonParseException || e instanceof JsonSyntaxException) {
            return new ApiException(ErrorCode.ERROR_PARSE,
                    context.getString(R.string.http_data_parse_error));
        }

        if (e instanceof SocketTimeoutException) {
            return new ApiException(ErrorCode.ERROR_NET_TIMEOUT,
                    context.getString(R.string.http_connect_timeout));
        }
        return new ApiException(ErrorCode.ERROR_UNKNOWN,
                context.getString(R.string.http_unknow_error));
    }
}
