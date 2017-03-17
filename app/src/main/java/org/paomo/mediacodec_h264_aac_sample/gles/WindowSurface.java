package org.paomo.mediacodec_h264_aac_sample.gles;

import android.view.Surface;

/**
 * Created by paomo on 2017/3/14.
 */

public class WindowSurface extends EglSurfaceBase {

    private Surface mSurface;
    private boolean mReleaseSurface;

    /**
     *  if you call releaseSurface true , the surfaceDestroy callback won't fire
     */
    public WindowSurface(EglCore eglCore, Surface surface,boolean releaseSurface) {
        super(eglCore);
        createWindowSurface(surface);
        this.mSurface = surface;
        this.mReleaseSurface = releaseSurface;
    }

}
