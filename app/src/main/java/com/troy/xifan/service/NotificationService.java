package com.troy.xifan.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.troy.xifan.R;
import com.troy.xifan.activity.MainActivity;
import com.troy.xifan.eventbus.NotificationEvent;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.model.response.NotificationRes;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenlongfei on 2017/3/24.
 */

public class NotificationService extends Service {
    private static final int NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        getNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    HttpRequestFactory.getInstance()
                            .getNotification(new SimpleHttpRequestCallback<NotificationRes>() {
                                @Override
                                public void onSuccess(NotificationRes responseData) {
                                    Logger.d(new Gson().toJson(responseData));
                                    try {
                                        int directMsg =
                                                Integer.parseInt(responseData.getDirect_messages());
                                        if (directMsg > 0) {
                                            createNotification(directMsg);
                                        }
                                    } catch (NumberFormatException e) {
                                    }
                                }

                                @Override
                                public void onFail(ApiException apiException) {
                                }
                            });

                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void createNotification(int msgCount) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_NEW_MSG, true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_logo);
        builder.setContentTitle(getString(R.string.title_dm));
        builder.setContentText(getString(R.string.text_notification, msgCount));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

        EventBus.getDefault().post(new NotificationEvent());
    }
}
