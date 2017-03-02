package com.troy.xifan.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.troy.xifan.api.ApiFactory;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.callback.HttpRequestCallback;
import com.troy.xifan.http.converter.ResponseConverterFactory;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.exception.ErrorCode;
import com.troy.xifan.http.exception.ExceptionHandle;
import com.troy.xifan.http.request.BaseRequestParams;
import com.troy.xifan.http.response.HttpResponseData;
import com.troy.xifan.util.XAuthUtils;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chenlongfei on 2016/11/19.
 */

public class HttpRequestFactory {
    //超时60s
    private static final long TIMEOUT = 60;
    private static HttpRequestFactory mInstance;
    private ApiFactory mServiceFactory;

    public HttpRequestFactory() {
        OkHttpClient kHttpClient =
                new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor())
                        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .addNetworkInterceptor(mTokenInterceptor)
                        .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.FanFou.FANFOU_API_URL)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(kHttpClient)
                .build();

        mServiceFactory = retrofit.create(ApiFactory.class);
    }

    public static HttpRequestFactory getInstance() {
        if (mInstance == null) {
            synchronized (HttpRequestFactory.class) {
                if (mInstance == null) {
                    mInstance = new HttpRequestFactory();
                }
            }
        }
        return mInstance;
    }

    public void getAccessToken(String username, String password, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getAccessToken(Constants.FanFou.ACCESS_TOKEN_URL), callback);
    }

    public void getHomeTimeline(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getHomeTimeline(requestParams.convertToQueryMap()), callback);
    }

    public void getUserHomeTimeline(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getUserTimeline(requestParams.convertToQueryMap()), callback);
    }

    public void getPublicTimeline(BaseRequestParams requestParams,HttpRequestCallback callback) {
        subscribe(mServiceFactory.getPublicTimeline(requestParams.convertToQueryMap()), callback);
    }

    public void destroyStatus(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.destroyStatus(requestParams.convertToBodyMap()), callback);
    }

    public void updateStatus(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.updateStatus(requestParams.convertToBodyMap()), callback);
    }

    public void uploadPhoto(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.uploadPhoto(requestParams.convertToBodyMap()), callback);
    }

    public void getUserPhotos(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getUserPhotos(requestParams.convertToQueryMap()), callback);
    }

    public void getMentions(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getMentions(requestParams.convertToQueryMap()), callback);
    }

    public void getConversationList(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getConversationList(requestParams.convertToQueryMap()), callback);
    }

    public void getConversation(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getConversation(requestParams.convertToQueryMap()), callback);
    }

    public void sendDirectMessage(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.sendDirectMessage(requestParams.convertToBodyMap()), callback);
    }

    public void createFavorite(String id, HttpRequestCallback callback) {
        subscribe(mServiceFactory.createFavorite(id), callback);
    }

    public void destroyFavorite(String id, HttpRequestCallback callback) {
        subscribe(mServiceFactory.destroyFavorite(id), callback);
    }

    public void getUserFavorites(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getUserFavorites(requestParams.convertToQueryMap()), callback);
    }

    public void getFriends(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getFriends(requestParams.convertToQueryMap()), callback);
    }

    public void getFollowers(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getFollowers(requestParams.convertToQueryMap()), callback);
    }

    public void getUserInfo(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.getUserInfo(requestParams.convertToQueryMap()), callback);
    }

    public void getTrends(HttpRequestCallback callback) {
        subscribe(mServiceFactory.getTrends(), callback);
    }

    public void searchPublicTimeline(BaseRequestParams requestParams,
            HttpRequestCallback callback) {
        subscribe(mServiceFactory.searchPublicTimeline(requestParams.convertToQueryMap()),
                callback);
    }

    public void searchUser(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.searchUser(requestParams.convertToQueryMap()), callback);
    }

    public void searchUserTimeline(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.searchUserTimeline(requestParams.convertToQueryMap()), callback);
    }

    public void createFriendship(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.createFriendship(requestParams.convertToBodyMap()), callback);
    }

    public void destroyFriendship(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.destoryFriendship(requestParams.convertToBodyMap()), callback);
    }

    public void isExistFriendship(BaseRequestParams requestParams, HttpRequestCallback callback) {
        subscribe(mServiceFactory.isExistFriendship(requestParams.convertToQueryMap()), callback);
    }

    private <T> void subscribe(Observable<HttpResponseData<T>> observable,
            HttpRequestCallback callback) {
        observable.map(new HttpResponseFunc<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new DoOnSubscribe(callback))
                .subscribeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpErrorResult<T>())
                .retryWhen(new RetryWithError())
                .subscribe(new HttpSubscriber<T>(callback));
    }

    //map变换
    private class HttpResponseFunc<T> implements Func1<HttpResponseData<T>, T> {

        @Override
        public T call(HttpResponseData<T> httpResponseData) {
            return httpResponseData.getData();
        }
    }

    //在事件发送前执行，用来在界面上显示loading
    private class DoOnSubscribe implements Action0 {
        private HttpRequestCallback mCallback;

        public DoOnSubscribe(HttpRequestCallback callback) {
            mCallback = callback;
        }

        @Override
        public void call() {
            mCallback.onStart();
        }
    }

    private class HttpSubscriber<T> extends Subscriber<T> {
        private HttpRequestCallback mCallback;

        public HttpSubscriber(HttpRequestCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof ApiException) {
                mCallback.onFail((ApiException) e);
            }
        }

        @Override
        public void onNext(T t) {
            mCallback.onSuccess(t);
        }
    }

    //异常预处理
    private class HttpErrorResult<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    //重试
    private class RetryWithError implements Func1<Observable<? extends Throwable>, Observable<?>> {
        //最大重试次数
        private int maxRetry = 3;
        //间隔时间ms
        private static final int INTERVAL = 1000;

        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {
            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    //网络超时重试
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;
                        if (apiException.getErrorCode().equals(ErrorCode.ERROR_NET_TIMEOUT)
                                && maxRetry-- > 0) {
                            return Observable.timer(INTERVAL, TimeUnit.MILLISECONDS);
                        }
                    }
                    return Observable.error(throwable);
                }
            });
        }
    }

    //TokenInterceptor add authorization
    private Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();
            Request tokenRequest = builder.header(Constants.HeaderName.AUTHORIZATION,
                    XAuthUtils.getAuthorization(originalRequest)).build();
            return chain.proceed(tokenRequest);
        }
    };
}
