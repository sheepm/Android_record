package org.paomo.mediacodec_h264_aac_sample.gles;

import android.opengl.EGL14;
import android.opengl.EGLSurface;

/**
 * Created by paomo on 2017/3/14.
 * there can be multi surface associated with a single context
 */
public class EglSurfaceBase {

    private EglCore mEglCore;

    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;

    public EglSurfaceBase(EglCore eglCore){
        mEglCore = eglCore;
    }

    /**
     *
     * @param surface may be surface or surfaceTexture
     */
    public void createWindowSurface(Object surface){
        if (mEGLSurface != null){
            throw new IllegalStateException("surface already setup");
        }
        mEGLSurface = mEglCore.createWindowSurface(surface);
    }

    /**
     * makes our EGL context and surface current
     */
    public void makeCurrent(){
        mEglCore.makeCurrent(mEGLSurface);
    }
}
