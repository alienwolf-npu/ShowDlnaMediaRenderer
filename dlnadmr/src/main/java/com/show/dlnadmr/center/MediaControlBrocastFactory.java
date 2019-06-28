package com.show.dlnadmr.center;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MediaControlBrocastFactory {

    public static final String MEDIA_RENDERER_CMD_PLAY = "com.show.dlnadmr.center.play_command";
    public static final String MEDIA_RENDERER_CMD_PAUSE = "com.show.dlnadmr.center.pause_command";
    public static final String MEDIA_RENDERER_CMD_STOP = "com.show.dlnadmr.center.stop_command";
    public static final String MEDIA_RENDERER_CMD_SEEK = "com.show.dlnadmr.center.seek_command";
    public static final String CMD_GET_PARAM_SEEK = "get_param_seek";
    private MediaControlBrocastReceiver mMediaControlReceiver;
    private Context mContext;

    public MediaControlBrocastFactory(Context context) {
        mContext = context;
    }

    public static void sendPlayBrocast(Context context) {
        Intent intent = new Intent(MEDIA_RENDERER_CMD_PLAY);
        context.sendBroadcast(intent);
    }

    public static void sendPauseBrocast(Context context) {
        Intent intent = new Intent(MEDIA_RENDERER_CMD_PAUSE);
        context.sendBroadcast(intent);
    }

    public static void sendStopBorocast(Context context) {
        Intent intent = new Intent(MEDIA_RENDERER_CMD_STOP);
        context.sendBroadcast(intent);
    }

    public static void sendSeekBrocast(Context context, int seekPos) {
        Intent intent = new Intent(MEDIA_RENDERER_CMD_SEEK);
        intent.putExtra(CMD_GET_PARAM_SEEK, seekPos);
        context.sendBroadcast(intent);
    }

    public void register(IMediaControlListener listener) {
        if (mMediaControlReceiver == null) {
            mMediaControlReceiver = new MediaControlBrocastReceiver();
            mMediaControlReceiver.setMediaControlListener(listener);

            mContext.registerReceiver(mMediaControlReceiver,
                    new IntentFilter(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_PLAY));
            mContext.registerReceiver(mMediaControlReceiver,
                    new IntentFilter(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_PAUSE));
            mContext.registerReceiver(mMediaControlReceiver,
                    new IntentFilter(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_STOP));
            mContext.registerReceiver(mMediaControlReceiver,
                    new IntentFilter(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_SEEK));

        }
    }

    public void unregister() {
        if (mMediaControlReceiver != null) {
            mContext.unregisterReceiver(mMediaControlReceiver);
            mMediaControlReceiver = null;
        }
    }

    public interface IMediaControlListener {
        void onPlayCommand();

        void onPauseCommand();

        void onStopCommand();

        void onSeekCommand(int time);
    }

}
