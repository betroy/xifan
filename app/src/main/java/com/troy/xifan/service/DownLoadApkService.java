package com.troy.xifan.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.troy.xifan.R;
import java.io.File;

/**
 * Created by chenlongfei on 2017/3/17.
 */

public class DownLoadApkService extends Service {
    public static final String EXTRA_URL = "url";
    public static final String APK_NAME = "xifan.apk";

    private DownloadManager mDownloadManager;
    private DownloadApkReceiver mDownloadApkReceiver;
    private long mEnqueue = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadApkReceiver = new DownloadApkReceiver();
        registerReceiver(mDownloadApkReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return START_REDELIVER_INTENT;
        }

        String url = bundle.getString(EXTRA_URL);
        if (!TextUtils.isEmpty(url)) {
            downLoadApk(url);
        }
        //服务被kill,服务将自动重启，并传入最后一个Intent
        return START_REDELIVER_INTENT;
    }

    private void downLoadApk(String url) {
        if (mEnqueue == 0) {
            mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(getString(R.string.app_name));
            request.setDescription(getString(R.string.text_downloading));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_NAME);
            request.setVisibleInDownloadsUi(true);
            mEnqueue = mDownloadManager.enqueue(request);
        }
    }

    public class DownloadApkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(mEnqueue);
                Cursor cursor = mDownloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        installApk();
                    }
                }
                stopSelf();
            }
        }
    }

    private void installApk() {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                APK_NAME);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file.toString()),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadApkReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
