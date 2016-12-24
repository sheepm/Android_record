package org.paomo.mediacodec_h264_aac_sample.codec;

import android.media.MediaCodec;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.view.Surface;

import java.io.File;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class EncodeAndMuxTest extends AndroidTestCase {

    private static final String TAG = "EncodeAndMuxTest";

    private static final File OUTPUT_DIR = Environment.getExternalStorageDirectory();

    private static final String MIME_TYPE = "video/avc";
    private static final int FRAME_RATE = 15;
    private static final int IFRAME_INTERVAL = 10;
    private static final int NUM_FRAMS = 30;

    private static final int TEST_R0 = 0;
    private static final int TEST_G0 = 136;
    private static final int TEST_B0 = 0;
    private static final int TEST_R1 = 236;
    private static final int TEST_G1 = 50;
    private static final int TEST_B1 = 186;

    private int mWidth = -1;
    private int mHeight = -1;

    private int mBitRate = -1;

    private MediaCodec mEncoder;

    private static class CodecInputSurface {
        private static final int EGL_RECORDABLE_ANDROID = 0x3142;

        private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
        private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;

        private Surface mSurface;

        public CodecInputSurface(Surface surface) {
            if (surface == null) {
                throw new NullPointerException();
            }
            mSurface = surface;
            eglSetup();
        }

        private void eglSetup() {
            mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
                throw new RuntimeException("unable to get EGL14 display");
            }
            int[] version = new int[2];
            if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
                throw new RuntimeException("unable to initialize EGL14");
            }
            //Configure EGL for recording and OpenGL ES 2.0
            int[] attribList = {
                    EGL14.EGL_RED_SIZE, 8,
                    EGL14.EGL_GREEN_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_ALPHA_SIZE, 8,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL_RECORDABLE_ANDROID, 1,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            EGL14.eglChooseConfig(mEGLDisplay,attribList,0,configs,0,configs.length,numConfigs,0);
            checkEglError("eglCreateContext RGB888 + recordable ES2");
            //Configure Context for OpenGL ES 2.0
            int[] attrib_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION,2,
                    EGL14.EGL_NONE
            };
            mEGLContext = EGL14.eglCreateContext(mEGLDisplay,configs[0],EGL14.EGL_NO_CONTEXT,attrib_list,0);
            checkEglError("eglCreateContext");
            //Create a window surface ,attach it to the surface received
            int[] surfaceAttribs = {
                    EGL14.EGL_NONE
            };
            mEGLSurface = EGL14.eglCreateWindowSurface(mEGLDisplay,configs[0],mSurface,surfaceAttribs,0);
            checkEglError("eglCreateWindowSurface");
        }

        public void release(){
            if (mEGLDisplay != EGL14.EGL_NO_DISPLAY){
                EGL14.eglMakeCurrent(mEGLDisplay,EGL14.EGL_NO_SURFACE,EGL14.EGL_NO_SURFACE,EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroySurface(mEGLDisplay,mEGLSurface);
                EGL14.eglDestroyContext(mEGLDisplay,mEGLContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(mEGLDisplay);
            }

            mSurface.release();

            mEGLDisplay = EGL14.EGL_NO_DISPLAY;
            mEGLSurface = EGL14.EGL_NO_SURFACE;
            mEGLContext = EGL14.EGL_NO_CONTEXT;

            mSurface = null;
        }

        public void makeCurrent(){
            EGL14.eglMakeCurrent(mEGLDisplay,mEGLSurface,mEGLSurface,mEGLContext);
            checkEglError("eglMakeCurrent");
        }

        public boolean swapBuffers(){
            boolean result = EGL14.eglSwapBuffers(mEGLDisplay,mEGLSurface);
            checkEglError("eglSwapBuffers");
            return result;
        }

        /**
         * send pts to EGL , nanoseconds
         */
        public void setPresentationTime(long nsecs){
            EGLExt.eglPresentationTimeANDROID(mEGLDisplay,mEGLSurface,nsecs);
            checkEglError("eglPresentationTimeANDROID");
        }

        private void checkEglError(String msg){
            int error;
            if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS){
                throw new RuntimeException(msg + ":EGL ERROR : 0x " +Integer.toHexString(error));
            }
        }
    }


}
