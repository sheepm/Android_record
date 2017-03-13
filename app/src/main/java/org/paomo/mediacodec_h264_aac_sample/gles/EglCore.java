package org.paomo.mediacodec_h264_aac_sample.gles;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;

/**
 * Created by paomo on 2017/3/13.
 */

public final class EglCore {

    private static final String TAG = "EglCore";

    private static final int FLAG_TRY_GLES3 = 0x02;

    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLConfig mEGLConfig = null;

    public EglCore() {
        this(null, 0);
    }

    public EglCore(EGLContext sharedContext, int flags) {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY){
            throw new RuntimeException("egl already set up");
        }
        if (sharedContext == null){
            sharedContext = EGL14.EGL_NO_CONTEXT;
        }
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY){
            throw new RuntimeException("can not get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEGLDisplay,version,0,version,1)){
            mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        if ((flags & FLAG_TRY_GLES3) != 0){

        }
    }
}
