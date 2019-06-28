package com.show.dlnadmr.center;

public interface IMediaRendererAction {
    void onRenderAvTransport(String value, String data);

    void onRenderPlay(String value, String data);

    void onRenderPause(String value, String data);

    void onRenderStop(String value, String data);

    void onRenderSeek(String value, String data);

    void onRenderSetMute(String value, String data);

    void onRenderSetVolume(String value, String data);
}

