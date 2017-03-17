package org.paomo.mediacodec_h264_aac_sample.gles;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

/**
 * Created by paomo on 2017/3/13.
 */

public final class EglCore {

    private static final String TAG = "EglCore";

    public static final int FLAG_RECORDABLE = 0x01;
    private static final int FLAG_TRY_GLES3 = 0x02;

    private static final int EGL_RECORDABLE_ANDROID = 0x3142;

    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLConfig mEGLConfig = null;

    private int mGlVersion = -1;

    public EglCore() {
        this(null, 0);
    }

    public EglCore(EGLContext sharedContext, int flags) {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("egl already set up");
        }
        if (sharedContext == null) {
            sharedContext = EGL14.EGL_NO_CONTEXT;
        }
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("can not get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        if ((flags & FLAG_TRY_GLES3) != 0) {
            EGLConfig config = getConfig(flags, 3);
            if (config != null) {
                int[] attrib3_list = {
                        EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
                        EGL14.EGL_NONE
                };
                EGLContext context = EGL14.eglCreateContext(mEGLDisplay, config, sharedContext, attrib3_list, 0);
                if (EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
                    mEGLConfig = config;
                    mEGLContext = context;
                    mGlVersion = 3;
                }
            }
        }
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            EGLConfig config = getConfig(flags, 2);
            if (config == null) {
                throw new RuntimeException("unable to find a suitable EGLConfig");
            }
            int[] attrib2_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            EGLContext context = EGL14.eglCreateContext(mEGLDisplay, config, sharedContext, attrib2_list, 0);
            checkEglError("eglCreateContext");
            mEGLContext = context;
            mEGLConfig = config;
            mGlVersion = 2;
        }

        int[] values = new int[2];
        EGL14.eglQueryContext(mEGLDisplay, mEGLContext, EGL14.EGL_CONTEXT_CLIENT_VERSION, values, 0);
    }

    private EGLConfig getConfig(int flags, int version) {
        int renderType = EGL14.EGL_OPENGL_ES2_BIT;
        if (version >= 3) {
            renderType |= EGLExt.EGL_OPENGL_ES3_BIT_KHR;
        }

        int[] attrList = {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, renderType,
                EGL14.EGL_NONE, 0, //place holder
                EGL14.EGL_NONE
        };
        if ((flags & FLAG_RECORDABLE) != 0) {
            attrList[attrList.length - 3] = EGL_RECORDABLE_ANDROID;
            attrList[attrList.length - 2] = 1;
        }
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(mEGLDisplay, attrList, 0, configs, 0, configs.length, numConfigs, 0)) {
            return null;
        }
        return configs[0];
    }

    public EGLSurface createWindowSurface(Object surface){
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)){
            throw new RuntimeException("surface invalid");
        }
        int[] surfaceAttribs = {
                EGL14.EGL_NONE
        };
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface, surfaceAttribs, 0);
        checkEglError("eglCreateWindowSurface");
        if (eglSurface == null){
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    public void makeCurrent(EGLSurface surface){
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY){
            Log.e(TAG,"make current before display");
        }
        if (!EGL14.eglMakeCurrent(mEGLDisplay,surface,surface,mEGLContext)){
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    private void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }
}
