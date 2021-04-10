package com.kamaii.customer.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service {

    private static final String TAG = "MyService";
    private boolean isRunning  = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;
    @Override
    public void onCreate() {
        HandlerThread handlerthread = new HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerthread.start();
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startId;
        myServiceHandler.sendMessage(msg);

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        isRunning = false;
    }
    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                for (int i = 0; i < 33; i++) {
                    try {
                        Thread.sleep(1000);
                        if(i==32){
                            stopSelf();
                            stopSelfResult(msg.arg1);
                        }

                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }

                }
            }
            stopSelfResult(msg.arg1);
        }
    }
}