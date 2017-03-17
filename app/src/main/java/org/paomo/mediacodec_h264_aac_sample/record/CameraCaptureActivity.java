package org.paomo.mediacodec_h264_aac_sample.record;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * Created by paomo on 2017/3/14.
 */

public class CameraCaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static class CameraHandler extends Handler{

        public static final int MSG_SET_SURFACE_TEXTURE = 0;

        private WeakReference<CameraCaptureActivity> mWeakActivity;

        public CameraHandler(CameraCaptureActivity activity){
            mWeakActivity = new WeakReference<CameraCaptureActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;

            CameraCaptureActivity activity = mWeakActivity.get();
            if (activity == null){
                return;
            }
            switch (what){
                case MSG_SET_SURFACE_TEXTURE:
                    break;
                default:
                    throw new RuntimeException("unknown msg");
            }
        }
    }
}
