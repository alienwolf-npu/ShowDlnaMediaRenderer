package com.show.dlnadmr.center;

import com.show.dlnadmr.IPlayerLauncher;
import com.show.dlnadmr.service.MediaRendererService;

import android.content.Context;
import android.content.Intent;

public class MediaRenderProxy implements IMediaRendererEngine {

    private static MediaRenderProxy mInstance;
    private static IPlayerLauncher mPlayerLauncher;
    private Context mContext;

    private MediaRenderProxy(Context context) {
        mContext = context;
    }

    public static synchronized MediaRenderProxy getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MediaRenderProxy(context);
        }
        return mInstance;
    }

    public static void registerPlayerLauncher(IPlayerLauncher launcher) {
        mPlayerLauncher = launcher;
    }

    public static IPlayerLauncher getPlayerLauncher() {
        return mPlayerLauncher;
    }

    @Override
    public boolean startEngine() {
        Intent intent = new Intent(MediaRendererService.START_RENDER_ENGINE);
        intent.setPackage(mContext.getPackageName());
        mContext.startService(intent);
        return true;
    }

    @Override
    public boolean stopEngine() {
        mContext.stopService(new Intent(mContext, MediaRendererService.class));
        return true;
    }

    @Override
    public boolean restartEngine() {
        Intent intent = new Intent(MediaRendererService.RESTART_RENDER_ENGINE);
        intent.setPackage(mContext.getPackageName());
        mContext.startService(intent);
        return true;
    }
}
