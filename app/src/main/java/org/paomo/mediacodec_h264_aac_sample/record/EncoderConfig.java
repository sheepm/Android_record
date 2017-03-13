package org.paomo.mediacodec_h264_aac_sample.record;

import android.opengl.EGLContext;

import java.io.File;

/**
 * Created by paomo on 2017/3/13.
 */

public class EncoderConfig {

    final File mOutputFile;
    final int mWidth;
    final int mHeight;
    final int mBitRate;
    final EGLContext mEglContext;

    public EncoderConfig(File outputFile,int width,int height,int bitRate,EGLContext eglContext){
        mOutputFile = outputFile;
        mWidth = width;
        mHeight = height;
        mBitRate = bitRate;
        mEglContext = eglContext;
    }

    @Override
    public String toString() {
        return "EncoderConfig{" +
                "mOutputFile=" + mOutputFile.toString() +
                ", mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mBitRate=" + mBitRate +
                ", mEglContext=" + mEglContext +
                '}';
    }
}
