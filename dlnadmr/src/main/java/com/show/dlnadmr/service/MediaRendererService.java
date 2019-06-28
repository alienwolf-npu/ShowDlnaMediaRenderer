package com.show.dlnadmr.service;

import com.show.dlnadmr.center.DLNAGenaEventBrocastFactory;
import com.show.dlnadmr.center.ActionCenter;
import com.show.dlnadmr.center.DlnaDmrEngine;
import com.show.dlnadmr.center.IMediaRendererEngine;
import com.show.dlnadmr.jni.PlatinumReflection;
import com.show.dlnadmr.jni.PlatinumReflection.ActionReflectionListener;
import com.show.dlnadmr.util.DlnaUtils;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MediaRendererService extends Service implements IMediaRendererEngine {

    public static final String TAG = "MediaRendererService";

    public static final String START_RENDER_ENGINE = "com.show.dlnadmr.start.engine";
    public static final String RESTART_RENDER_ENGINE = "com.show.dlnadmr.restart.engine";
    private static final int START_ENGINE_MSG_ID = 0x0001;
    private static final int RESTART_ENGINE_MSG_ID = 0x0002;
    private static final int DELAY_TIME = 1000;
    private DlnaDmrEngine mEngine;
    private ActionReflectionListener mListener;
    private DLNAGenaEventBrocastFactory mMediaGenaBrocastFactory;
    private Handler mHandler;
    private MulticastLock mMulticastLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRenderService();
        Log.i(TAG, "MediaRendererService onCreate");
    }

    @Override
    public void onDestroy() {
        unInitRenderService();
        Log.i(TAG, "MediaRendererService onDestroy");
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String actionString = intent.getAction();
            if (actionString != null) {
                if (actionString.equalsIgnoreCase(START_RENDER_ENGINE)) {
                    delayToSendStartMsg();
                } else if (actionString.equalsIgnoreCase(RESTART_RENDER_ENGINE)) {
                    delayToSendRestartMsg();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    private void initRenderService() {

        mListener = new ActionCenter(this);
        PlatinumReflection.setActionInvokeListener(mListener);
        mMediaGenaBrocastFactory = new DLNAGenaEventBrocastFactory(this);
        mMediaGenaBrocastFactory.registerHandler();
        mEngine = new DlnaDmrEngine(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case START_ENGINE_MSG_ID:
                        startEngine();
                        break;
                    case RESTART_ENGINE_MSG_ID:
                        restartEngine();
                        break;
                }
            }

        };

        mMulticastLock = DlnaUtils.openWifiBrocast(this);
        Log.i(TAG, "openWifiBrocast = " + (mMulticastLock != null));
    }

    private void unInitRenderService() {
        stopEngine();
        removeStartMsg();
        removeRestartMsg();
        mMediaGenaBrocastFactory.unRegisterHandler();
        if (mMulticastLock != null) {
            mMulticastLock.release();
            mMulticastLock = null;
            Log.i(TAG, "closeWifiBrocast");
        }
    }

    private void delayToSendStartMsg() {
        removeStartMsg();
        mHandler.sendEmptyMessageDelayed(START_ENGINE_MSG_ID, DELAY_TIME);
    }

    private void delayToSendRestartMsg() {
        removeStartMsg();
        removeRestartMsg();
        mHandler.sendEmptyMessageDelayed(RESTART_ENGINE_MSG_ID, DELAY_TIME);
    }

    private void removeStartMsg() {
        mHandler.removeMessages(START_ENGINE_MSG_ID);
    }

    private void removeRestartMsg() {
        mHandler.removeMessages(RESTART_ENGINE_MSG_ID);
    }

    @Override
    public boolean startEngine() {
        awakeWorkThread();
        return true;
    }

    @Override
    public boolean stopEngine() {
        mEngine.setParam("", "");
        exitWorkThread();
        return true;
    }

    @Override
    public boolean restartEngine() {
        String friendName = DlnaUtils.getDevName(this);
        String uuid = DlnaUtils.getDevUUID(this);
        mEngine.setParam(friendName, uuid);
        if (mEngine.isAlive()) {
            mEngine.restartEngine();
        } else {
            mEngine.start();
        }
        return true;
    }

    private void awakeWorkThread() {
        String friendName = DlnaUtils.getDevName(this);
        String uuid = DlnaUtils.getDevUUID(this);
        mEngine.setParam(friendName, uuid);

        if (mEngine.isAlive()) {
            mEngine.awakeThread();
        } else {
            mEngine.start();
        }
    }

    private void exitWorkThread() {
        if (mEngine != null && mEngine.isAlive()) {
            mEngine.exit();
            long time1 = System.currentTimeMillis();
            while (mEngine.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long time2 = System.currentTimeMillis();
            Log.i(TAG, "exitWorkThread cost time:" + (time2 - time1));
            mEngine = null;
        }
    }

}
