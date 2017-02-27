package com.troy.xifan.http.exception;

/**
 * Created by chenlongfei on 2016/11/27.
 */

public class ErrorCode {
    //登录失败
    public static String ERROR_UNAUTHORIZED = "1000";

    //网络未连接
    public static String ERROR_NO_CONNECT = "1001";

    //网络未注册
    public static String ERROR_NO_REGISTER = "1002";

    //未能连接到网络
    public static String ERROR_NET_ACCESS = "1003";

    //网络请求超时
    public static String ERROR_NET_TIMEOUT = "1004";

    //用户取消
    public static String USER_CANCELLED = "1005";

    //系统取消
    public static String SYSTEM_CANCELLED = "1006";

    //错误的服务访问
    public static String ERROR_SERVICE_ACCESS = "1007";

    // 需要重试
    public static String ERROR_NEED_RETRY_IP = "1008";

    //数据解析失败
    public static String ERROR_PARSE = "1009";

    //未知
    public static String ERROR_UNKNOWN = "1010";
}
