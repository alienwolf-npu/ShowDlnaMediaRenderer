//
// Created by ztq on 19-5-20.
//

#ifndef AUTOPARSEDCMAKEPROJECT_PLATINUM_ANDROID_DEF_H
#define AUTOPARSEDCMAKEPROJECT_PLATINUM_ANDROID_DEF_H

#ifdef __cplusplus
extern  "C" {
#endif

enum MediaRenderControlType {
    MEDIA_RENDER_CTL_MSG_BASE = 0x100,
    MEDIA_RENDER_CTL_MSG_SET_AV_URL = MEDIA_RENDER_CTL_MSG_BASE + 0,
    MEDIA_RENDER_CTL_MSG_STOP = MEDIA_RENDER_CTL_MSG_BASE + 1,
    MEDIA_RENDER_CTL_MSG_PLAY = MEDIA_RENDER_CTL_MSG_BASE + 2,
    MEDIA_RENDER_CTL_MSG_PAUSE = MEDIA_RENDER_CTL_MSG_BASE + 3,
    MEDIA_RENDER_CTL_MSG_SEEK = MEDIA_RENDER_CTL_MSG_BASE + 4,
    MEDIA_RENDER_CTL_MSG_SETVOLUME = MEDIA_RENDER_CTL_MSG_BASE + 5,
    MEDIA_RENDER_CTL_MSG_SETMUTE = MEDIA_RENDER_CTL_MSG_BASE + 6,
    MEDIA_RENDER_CTL_MSG_SETPLAYMODE = MEDIA_RENDER_CTL_MSG_BASE + 7,
    MEDIA_RENDER_CTL_MSG_PRE = MEDIA_RENDER_CTL_MSG_BASE + 8,
    MEDIA_RENDER_CTL_MSG_NEXT = MEDIA_RENDER_CTL_MSG_BASE + 9
};

enum MediaRenderGenaCmd {
    MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION = 0x100,
    MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_POSITION = MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION + 1,
    MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE = MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION + 2,
	MEDIA_RENDER_TOCONTRPOINT_SET_MUTE = MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION + 3,
    MEDIA_RENDER_TOCONTRPOINT_SET_VOLUME = MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION + 4,
	MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_UI = MEDIA_RENDER_CTL_MSG_BASE + 5,
	MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_META = MEDIA_RENDER_CTL_MSG_BASE + 6,
	MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_TRACK = MEDIA_RENDER_CTL_MSG_BASE + 7
};

#ifdef __cplusplus
}
#endif
#endif //AUTOPARSEDCMAKEPROJECT_PLATINUM_ANDROID_DEF_H
