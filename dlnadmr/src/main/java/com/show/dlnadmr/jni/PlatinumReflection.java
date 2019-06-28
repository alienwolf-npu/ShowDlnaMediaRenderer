package com.show.dlnadmr.jni;

public class PlatinumReflection {

    /*----------------------------------------------------------------*/
    public static final String RENDERER_TOCONTRPOINT_CMD_INTENT_NAME = "com.show.dlnadmr.tocontrolpointer"
            + ".cmd.intent";
    public static final String GET_RENDERER_TOCONTRPOINT_CMD = "get_dlna_renderer_tocontrolpointer.cmd";
    public static final String GET_PARAM_MEDIA_DURATION = "get_param_media_duration";
    public static final String GET_PARAM_MEDIA_POSITION = "get_param_media_position";
    public static final String GET_PARAM_MEDIA_PLAYINGSTATE = "get_param_media_playingstate";
    public static final String GET_PARAM_MUTE = "get_param_mute";
    public static final String GET_PARAM_VOLUME = "get_param_volume";
    public static final String MEDIA_PLAYINGSTATE_STOP = "STOPPED";
    public static final String MEDIA_PLAYINGSTATE_PAUSE = "PAUSED_PLAYBACK";
    public static final String MEDIA_PLAYINGSTATE_PLAYING = "PLAYING";
    public static final String MEDIA_PLAYINGSTATE_TRANSTION = "TRANSITIONING";
    public static final String MEDIA_PLAYINGSTATE_NOMEDIA = "NO_MEDIA_PRESENT";

    public static final String MEDIA_SEEK_TIME_TYPE_REL_TIME = "REL_TIME";
    public static final String MEDIA_SEEK_TIME_TYPE_TRACK_NR = "TRACK_NR";
    public static final String GET_PARAM_CURRENT_TRACK_URI = "get_param_current_track_uri";
    public static final String GET_PARAM_CURRENT_TRACK_NUM = "get_param_current_track_NUM";
    public static final String GET_PARAM_CURRENT_TRACK_META = "get_param_current_track_META";

    private static final int MEDIA_RENDER_CTL_MSG_BASE = 0x100;

    public static final int MEDIA_RENDER_CTL_MSG_SET_AV_URL = (MEDIA_RENDER_CTL_MSG_BASE + 0);
    public static final int MEDIA_RENDER_CTL_MSG_STOP = (MEDIA_RENDER_CTL_MSG_BASE + 1);
    public static final int MEDIA_RENDER_CTL_MSG_PLAY = (MEDIA_RENDER_CTL_MSG_BASE + 2);
    public static final int MEDIA_RENDER_CTL_MSG_PAUSE = (MEDIA_RENDER_CTL_MSG_BASE + 3);
    public static final int MEDIA_RENDER_CTL_MSG_SEEK = (MEDIA_RENDER_CTL_MSG_BASE + 4);
    public static final int MEDIA_RENDER_CTL_MSG_SETVOLUME = (MEDIA_RENDER_CTL_MSG_BASE + 5);
    public static final int MEDIA_RENDER_CTL_MSG_SETMUTE = (MEDIA_RENDER_CTL_MSG_BASE + 6);
    public static final int MEDIA_RENDER_CTL_MSG_SETPLAYMODE = (MEDIA_RENDER_CTL_MSG_BASE + 7);

    public static final int MEDIA_RENDER_CTL_MSG_PRE = (MEDIA_RENDER_CTL_MSG_BASE + 8);
    public static final int MEDIA_RENDER_CTL_MSG_NEXT = (MEDIA_RENDER_CTL_MSG_BASE + 9);

    public static final int MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION = (MEDIA_RENDER_CTL_MSG_BASE + 0);
    public static final int MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_POSITION = (MEDIA_RENDER_CTL_MSG_BASE + 1);
    public static final int MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE = (MEDIA_RENDER_CTL_MSG_BASE + 2);
    public static final int MEDIA_RENDER_TOCONTRPOINT_SET_MUTE = (MEDIA_RENDER_CTL_MSG_BASE + 3);
    public static final int MEDIA_RENDER_TOCONTRPOINT_SET_VOLUME = (MEDIA_RENDER_CTL_MSG_BASE + 4);
    public static final int MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_UI = (MEDIA_RENDER_CTL_MSG_BASE + 5);
    public static final int MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_META = (MEDIA_RENDER_CTL_MSG_BASE + 6);
    public static final int MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_TRACK = (MEDIA_RENDER_CTL_MSG_BASE + 7);

    private static ActionReflectionListener mListener;

    public static void onActionReflection(int cmd, String value, String data) {
        if (mListener != null) {
            mListener.onActionInvoke(cmd, value, data);
        }
    }

    public static void setActionInvokeListener(ActionReflectionListener listener) {
        mListener = listener;
    }

    public static interface ActionReflectionListener {
        void onActionInvoke(int cmd, String value, String data);
    }
}
