package org.paomo.mediacodec_h264_aac_sample.record;

import android.opengl.EGLContext;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.paomo.mediacodec_h264_aac_sample.gles.EglCore;
import org.paomo.mediacodec_h264_aac_sample.gles.WindowSurface;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by paomo on 2017/3/13.
 */

public class TextureMovieEncoder implements Runnable {

    private static final String TAG = "TextureMovieEncoder";

    private static final int MSG_START_RECORDING = 0;
    private static final int MSG_STOP_RECORDING = 1;
    private static final int MSG_FRAME_AVAILABLE = 2;
    private static final int MSG_SET_TEXTURE_ID = 3;
    private static final int MSG_UPDATE_SHARED_CONTEXT = 4;
    private static final int MSG_QUIT = 5;

    private EncoderHandler mHandler;
    private VideoEncoderCore mVideoEncoder;
    private EglCore mEglCore;
    private WindowSurface mInputWindowSurface;

    private Object mReadFence = new Object();
    private boolean mRunning;
    private boolean mReady;

    private int mFrameNum;

    public void startRecord(EncoderConfig config) {
        synchronized (mReadFence) {
            if (mRunning) {
                Log.e(TAG, "record already start");
                return;
            }
            mRunning = true;
            new Thread(this, "TextureMovieEncoder").start();
            while (!mReady) {
                try {
                    mReadFence.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_START_RECORDING, config));
    }

    @Override
    public void run() {
        Looper.prepare();
        synchronized (mReadFence) {
            mHandler = new EncoderHandler(this);
            mReady = true;
            mReadFence.notify();
        }
        Looper.loop();

        synchronized (mReadFence) {
            mReadFence = mReady = false;
            mHandler = null;
        }
    }

    private void handleStartRecording(EncoderConfig config) {
        mFrameNum = 0;
        prepareEncoder(config.mEglContext, config.mWidth, config.mHeight, config.mBitRate, config.mOutputFile);
    }

    private void prepareEncoder(EGLContext sharedContext, int width, int height, int bitRate, File outputFile) {
        try {
            mVideoEncoder = new VideoEncoderCore(width,height,bitRate,outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEglCore = new EglCore(sharedContext,EglCore.FLAG_RECORDABLE);
        mInputWindowSurface = new WindowSurface(mEglCore,mVideoEncoder.getInputSurface(),true);
        mInputWindowSurface.makeCurrent();
    }

    private static class EncoderHandler extends Handler {
        private WeakReference<TextureMovieEncoder> mWeakEncoder;

        public EncoderHandler(TextureMovieEncoder encoder) {
            mWeakEncoder = new WeakReference<TextureMovieEncoder>(encoder);
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            Object obj = msg.obj;
            TextureMovieEncoder encoder = mWeakEncoder.get();
            if (encoder == null) {
                Log.e(TAG, "encoder is null");
                return;
            }
            switch (what) {
                case MSG_START_RECORDING:
                    encoder.handleStartRecording((EncoderConfig) obj);
                    break;
            }
        }
    }
}
