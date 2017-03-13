package org.paomo.mediacodec_h264_aac_sample.record;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.view.Surface;

import java.io.File;
import java.io.IOException;

/**
 * Created by paomo on 2017/3/13.
 */

public class VideoEncoderCore {

    private static final String TAG = "VideoEncoderCore";

    private static final String MIME_TYPE = "video/avc";
    private static final int FRAME_RATE = 30;
    private static final int I_FRAME_INTERVAL = 5;

    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mEncoder;
    private Surface mInputSurface;
    private MediaMuxer mMuxer;

    private int mTrackIndex;
    private boolean mMuxerStarted;

    public VideoEncoderCore(int width, int height, int bitRate, File outputFile) throws IOException {
        mBufferInfo = new MediaCodec.BufferInfo();

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, width, height);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL);
        //create MediaCodec encoder
        mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        mEncoder.configure(format,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);

        mInputSurface = mEncoder.createInputSurface();
        mEncoder.start();

        mMuxer = new MediaMuxer(outputFile.toString(),MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        mTrackIndex = -1;
        mMuxerStarted = false;
    }
}
