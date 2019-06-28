package com.show.dlnadmr;

import com.show.dlnadmr.center.DlnaMediaModel;

public interface IPlayerLauncher {
    void startMusicPlayer(DlnaMediaModel mediaInfo);

    void startVideoPlayer(DlnaMediaModel mediaInfo);

    void startImagePlayer(DlnaMediaModel mediaInfo);
}
