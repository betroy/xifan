package com.troy.xifan.http.exception;

/**
 * Created by chenlongfei on 2016/11/27.
 */

public class ApiException extends Exception {
    private String errorCode;
    private String errorMessage;

    public ApiException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
