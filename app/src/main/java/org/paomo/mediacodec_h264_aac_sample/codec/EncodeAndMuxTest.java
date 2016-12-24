package org.paomo.mediacodec_h264_aac_sample.codec;

import android.media.MediaCodec;
import android.os.Environment;
import android.test.AndroidTestCase;

import java.io.File;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class EncodeAndMuxTest extends AndroidTestCase{

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





































}
