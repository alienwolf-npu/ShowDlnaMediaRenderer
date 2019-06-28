package com.show.dlnadmr.center;

import com.show.dlnadmr.jni.PlatinumReflection;
import com.show.dlnadmr.jni.PlatinumReflection.ActionReflectionListener;
import com.show.dlnadmr.util.DlnaUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ActionCenter implements ActionReflectionListener, IMediaRendererAction {

    public static final String TAG = "ActionCenter";

    public static final int CUR_MEDIA_TYPE_MUSCI = 0x0001;
    public static final int CUR_MEDIA_TYPE_VIDEO = 0x0002;
    public static final int CUR_MEDIA_TYPE_PICTURE = 0x0003;
    private static final int DELAYTIME = 200;
    private static final int MSG_START_MUSICPLAY = 0x0001;
    private static final int MSG_START_PICPLAY = 0x0002;
    private static final int MSG_START_VIDOPLAY = 0x0003;
    private static final int MSG_SEND_STOPCMD = 0x0004;
    private Context mContext;
    private DlnaMediaModel mMusicMediaInfo;
    private DlnaMediaModel mVideoMediaInfo;
    private DlnaMediaModel mImageMediaInfo;
    private int mCurMediaInfoType = -1;
    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {

            try {
                switch (msg.what) {
                    case MSG_START_MUSICPLAY:
                        DlnaMediaModel mediaInfo1 = (DlnaMediaModel) msg.obj;
                        startPlayMusic(mediaInfo1);
                        break;
                    case MSG_START_PICPLAY:
                        DlnaMediaModel mediaInfo2 = (DlnaMediaModel) msg.obj;
                        startPlayPicture(mediaInfo2);
                        break;
                    case MSG_START_VIDOPLAY:
                        DlnaMediaModel mediaInfo3 = (DlnaMediaModel) msg.obj;
                        startPlayVideo(mediaInfo3);
                        break;
                    case MSG_SEND_STOPCMD:
                        MediaControlBrocastFactory.sendStopBorocast(mContext);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ActionCenter transdel msg catch Exception!!! msgID = " + msg.what);
            }

        }

    };

    public ActionCenter(Context context) {
        mContext = context;
    }

    @Override
    public synchronized void onActionInvoke(int cmd, String value, String data) {

        switch (cmd) {
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SET_AV_URL:
                onRenderAvTransport(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_PLAY:
                onRenderPlay(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_PAUSE:
                onRenderPause(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_STOP:
                onRenderStop(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SEEK:
                onRenderSeek(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SETMUTE:
                onRenderSetMute(value, data);
                break;
            case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SETVOLUME:
                onRenderSetVolume(value, data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRenderAvTransport(String value, String data) {
        if (data == null) {
            Log.e(TAG, "meteData = null!!!");
            return;
        }

        if (value == null || value.length() < 2) {
            Log.e(TAG, "url = " + value + ", it's invalid...");
            return;
        }

        DlnaMediaModel mediaInfo = DlnaMediaModelFactory.createFromMetaData(data);
        mediaInfo.setUrl(value);
        DLNAGenaEventBrocastFactory.sendCurrentTrackUriEvent(mContext, mediaInfo.getUrl());
        DLNAGenaEventBrocastFactory.sendCurrentTrackNumEvent(mContext, "1");
        DLNAGenaEventBrocastFactory.sendCurrentMetaEvent(mContext, data);
        if (DlnaUtils.isAudioItem(mediaInfo)) {
            mMusicMediaInfo = mediaInfo;
            mCurMediaInfoType = CUR_MEDIA_TYPE_MUSCI;
        } else if (DlnaUtils.isVideoItem(mediaInfo)) {
            mVideoMediaInfo = mediaInfo;
            mCurMediaInfoType = CUR_MEDIA_TYPE_VIDEO;
        } else if (DlnaUtils.isImageItem(mediaInfo)) {
            mImageMediaInfo = mediaInfo;
            mCurMediaInfoType = CUR_MEDIA_TYPE_PICTURE;
        } else {
            Log.e(TAG, "unknow media type!!! mediainfo.objectclass = \n" + mediaInfo.getObjectClass());
        }
    }

    @Override
    public void onRenderPlay(String value, String data) {
        switch (mCurMediaInfoType) {
            case CUR_MEDIA_TYPE_MUSCI:
                if (mMusicMediaInfo != null) {
                    delayToPlayMusic(mMusicMediaInfo);
                } else {
                    MediaControlBrocastFactory.sendPlayBrocast(mContext);
                }
                clearState();
                break;
            case CUR_MEDIA_TYPE_VIDEO:
                if (mVideoMediaInfo != null) {
                    delayToPlayVideo(mVideoMediaInfo);
                } else {
                    MediaControlBrocastFactory.sendPlayBrocast(mContext);
                }
                clearState();
                break;
            case CUR_MEDIA_TYPE_PICTURE:
                if (mImageMediaInfo != null) {
                    delayToPlayImage(mImageMediaInfo);
                } else {
                    MediaControlBrocastFactory.sendPlayBrocast(mContext);
                }
                clearState();
                break;
        }
    }

    @Override
    public void onRenderPause(String value, String data) {
        MediaControlBrocastFactory.sendPauseBrocast(mContext);
    }

    @Override
    public void onRenderStop(String value, String data) {
        MediaControlBrocastFactory.sendStopBorocast(mContext);
    }

    @Override
    public void onRenderSeek(String value, String data) {
        int seekPos = 0;
        try {
            seekPos = DlnaUtils.parseSeekTime(value);
            MediaControlBrocastFactory.sendSeekBrocast(mContext, seekPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRenderSetMute(String value, String data) {

        if ("1".equals(value)) {
            DlnaUtils.setVolumeMute(mContext);
        } else if ("0".equals(value)) {
            DlnaUtils.setVolumeUnmute(mContext);
        }
    }

    @Override
    public void onRenderSetVolume(String value, String data) {
        try {
            int volume = Integer.valueOf(value);
            if (volume < 101) {
                DlnaUtils.setCurrentVolume(volume, mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearState() {
        mMusicMediaInfo = null;
        mVideoMediaInfo = null;
        mImageMediaInfo = null;
    }

    private void delayToPlayMusic(DlnaMediaModel mediaInfo) {
        if (mediaInfo != null) {
            clearDelayMsg();
            Message msg = mHandler.obtainMessage(MSG_START_MUSICPLAY, mediaInfo);
            mHandler.sendMessageDelayed(msg, DELAYTIME);
        }
    }

    private void delayToPlayVideo(DlnaMediaModel mediaInfo) {
        if (mediaInfo != null) {
            clearDelayMsg();
            Message msg = mHandler.obtainMessage(MSG_START_VIDOPLAY, mediaInfo);
            mHandler.sendMessageDelayed(msg, DELAYTIME);
        }
    }

    private void delayToPlayImage(DlnaMediaModel mediaInfo) {
        if (mediaInfo != null) {
            clearDelayMsg();
            Message msg = mHandler.obtainMessage(MSG_START_PICPLAY, mediaInfo);
            mHandler.sendMessageDelayed(msg, DELAYTIME);
        }
    }

    private void delayToStop() {
        clearDelayMsg();
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SEND_STOPCMD), DELAYTIME);
    }

    private void clearDelayMsg() {
        clearDelayMsg(MSG_START_MUSICPLAY);
        clearDelayMsg(MSG_START_PICPLAY);
        clearDelayMsg(MSG_START_VIDOPLAY);
        clearDelayMsg(MSG_SEND_STOPCMD);
    }

    private void clearDelayMsg(int num) {
        mHandler.removeMessages(num);
    }

    private void startPlayMusic(DlnaMediaModel mediaInfo) {
        Log.i(TAG, "startPlayMusic");
        MediaRenderProxy.getPlayerLauncher().startMusicPlayer(mediaInfo);
    }

    private void startPlayVideo(DlnaMediaModel mediaInfo) {
        Log.i(TAG, "startPlayVideo");
        MediaRenderProxy.getPlayerLauncher().startVideoPlayer(mediaInfo);
    }

    private void startPlayPicture(DlnaMediaModel mediaInfo) {
        Log.i(TAG, "startPlayPicture");
        MediaRenderProxy.getPlayerLauncher().startImagePlayer(mediaInfo);
    }
}
