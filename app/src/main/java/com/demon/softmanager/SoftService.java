package com.demon.softmanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * @author DeMon
 * @date 2018/8/10
 * @description
 */
public class SoftService extends Service {
    private ActivityManager activityManager = null;
    private Handler handler = new Handler();
    private String activityName = null;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(1);
            activityName = (runningTaskInfo.get(0).topActivity).toString();
            Log.i("ActivityManager: ", "当前activity是---->" + activityName);

            handler.postDelayed(runnable, 5000);//每5秒刷新一次
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnable);//开启子线程
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
    }

}
