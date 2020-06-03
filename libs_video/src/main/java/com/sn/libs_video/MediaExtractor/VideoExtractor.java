package com.sn.libs_video.MediaExtractor;

import android.media.MediaFormat;

import com.sn.libs_video.MediaExtractor.MMExtractor;
import com.sn.libs_video.interfaces.IExtractor;

import java.nio.ByteBuffer;

public class VideoExtractor implements IExtractor {
    private MMExtractor mMediaExtractor;

    public VideoExtractor(String path) {
        mMediaExtractor = new MMExtractor(path);
    }


    @Override
    public MediaFormat getFormat() {
        return mMediaExtractor.getVideoFormat();
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mMediaExtractor.readBuffer(byteBuffer);
    }

    @Override
    public Long getCurrentTimestamp() {
        return mMediaExtractor.getCurrentTimestamp();
    }

    @Override
    public Long seek(long pos) {
        return mMediaExtractor.seek(pos);
    }

    @Override
    public void setStartPos(Long pos) {
        mMediaExtractor.setStartPos(pos);
    }

    @Override
    public void stop() {
        mMediaExtractor.stop();
    }
}
