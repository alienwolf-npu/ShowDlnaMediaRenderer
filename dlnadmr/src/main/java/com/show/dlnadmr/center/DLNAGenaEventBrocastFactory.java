package com.show.dlnadmr.center;

import com.show.dlnadmr.jni.PlatinumJniProxy;
import com.show.dlnadmr.jni.PlatinumReflection;
import com.show.dlnadmr.util.DlnaUtils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public class DLNAGenaEventBrocastFactory {
    private static HandlerThread mHandlerThread;
    private static Handler mHandler;
    private Context mContext;

    public DLNAGenaEventBrocastFactory(Context context) {
        mContext = context;
        mHandlerThread = new HandlerThread("DLNAGenaEventHandlerThread");
    }

    private static void postEventToControlPoint(final int cmd, final String value) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    PlatinumJniProxy.responseGenaEvent(cmd, value, null);
                }
            });
        }
    }

    public static void sendVolumeChangeEvent(Context context, int volume) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_VOLUME, String.valueOf(volume));
    }

    public static void sendSeekEvent(Context context, int time) {
        if (time != 0) {
            postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_POSITION,
                    DlnaUtils.formatTimeFromMSInt(time));
        }
    }

    public static void sendDurationEvent(Context context, int duration) {
        if (duration != 0) {
            postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION,
                    DlnaUtils.formatTimeFromMSInt(duration));
        }
    }

    public static void sendCurrentTrackUriEvent(Context context, String content) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_UI, content);
    }

    public static void sendCurrentTrackNumEvent(Context context, String content) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_TRACK, content);
    }

    public static void sendCurrentMetaEvent(Context context, String content) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_META, content);
    }

    public static void sendPlayStateEvent(Context context) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE,
                PlatinumReflection.MEDIA_PLAYINGSTATE_PLAYING);
    }

    public static void sendPauseStateEvent(Context context) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE,
                PlatinumReflection.MEDIA_PLAYINGSTATE_PAUSE);
    }

    public static void sendStopStateEvent(Context context) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE,
                PlatinumReflection.MEDIA_PLAYINGSTATE_STOP);
    }

    public static void sendTranstionEvent(Context context) {
        postEventToControlPoint(PlatinumReflection.MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE,
                PlatinumReflection.MEDIA_PLAYINGSTATE_TRANSTION);
    }

    public void registerHandler() {
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("DLNAGenaEventHandlerThread");
        }

        if (!mHandlerThread.isAlive()) {
            mHandlerThread.start();
        }

        if (mHandler == null) {
            mHandler = new Handler(mHandlerThread.getLooper());
        }
    }

    public void unRegisterHandler() {
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        mHandler = null;
    }
}
