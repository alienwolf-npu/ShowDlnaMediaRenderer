package com.show.dlnadmr.center;

import com.show.dlnadmr.jni.PlatinumJniProxy;
import com.show.dlnadmr.jni.PlatinumReflection;
import com.show.dlnadmr.util.DlnaUtils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class DlnaDmrEngine extends Thread implements IMediaRendererEngine {

    public static final String TAG = "DlnaDmrEngine";

    private static final int CHECK_INTERVAL = 30 * 1000;
    private Context mContext = null;
    private boolean mStartSuccess = false;
    private boolean mExitFlag = false;

    private String mFriendName = "";
    private String mUUID = "";

    public DlnaDmrEngine(Context context) {
        mContext = context;
    }

    public void setFlag(boolean flag) {
        mStartSuccess = flag;
    }

    public void setParam(String friendName, String uuid) {
        mFriendName = friendName;
        mUUID = uuid;
    }

    public void awakeThread() {
        synchronized(this) {
            notifyAll();
        }
    }

    public void exit() {
        mExitFlag = true;
        awakeThread();
    }

    @Override
    public void run() {

        Log.i(TAG, "DlnaDmrEngine run...");

        while (true) {
            if (mExitFlag) {
                stopEngine();
                break;
            }
            refreshNotify();
            synchronized(this) {
                try {
                    wait(CHECK_INTERVAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mExitFlag) {
                stopEngine();
                break;
            }
        }

        Log.i(TAG, "DlnaDmrEngine over...");

    }

    public void refreshNotify() {
        if (!DlnaUtils.checkNetworkState(mContext)) {
            return;
        }

        if (!mStartSuccess) {
            stopEngine();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean ret = startEngine();
            if (ret) {
                mStartSuccess = true;
            }
        }

    }

    @Override
    public boolean startEngine() {
        if (mFriendName.length() == 0) {
            return false;
        }

        int ret = PlatinumJniProxy.startMediaRender(mFriendName, mUUID);
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volume_value = (int) (currentVolume * 1.0f / maxVolume * 100);
        PlatinumJniProxy.responseGenaEvent(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_VOLUME,
                String.valueOf(volume_value), null);
        boolean result = (ret == 0 ? true : false);
        return result;
    }

    @Override
    public boolean stopEngine() {
        PlatinumJniProxy.stopMediaRender();
        return true;
    }

    @Override
    public boolean restartEngine() {
        setFlag(false);
        awakeThread();
        return true;
    }

}
