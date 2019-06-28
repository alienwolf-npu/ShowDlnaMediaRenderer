package com.show.dlnadmr.center;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MediaControlBrocastReceiver extends BroadcastReceiver {

    private MediaControlBrocastFactory.IMediaControlListener mMediaControlListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action != null && mMediaControlListener != null) {
            processControlCommand(intent);
        }
    }

    public void setMediaControlListener(MediaControlBrocastFactory.IMediaControlListener listener) {
        mMediaControlListener = listener;
    }

    private void processControlCommand(Intent intent) {
        int time = 0;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_PLAY)) {
            mMediaControlListener.onPlayCommand();
        } else if (action.equalsIgnoreCase(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_PAUSE)) {
            mMediaControlListener.onPauseCommand();
        } else if (action.equalsIgnoreCase(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_STOP)) {
            mMediaControlListener.onStopCommand();
        } else if (action.equalsIgnoreCase(MediaControlBrocastFactory.MEDIA_RENDERER_CMD_SEEK)) {
            time = intent.getIntExtra(MediaControlBrocastFactory.CMD_GET_PARAM_SEEK, 0);
            mMediaControlListener.onSeekCommand(time);
        }

    }

}
