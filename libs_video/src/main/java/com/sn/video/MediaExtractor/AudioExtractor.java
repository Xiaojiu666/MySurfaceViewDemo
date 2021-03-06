package com.sn.video.MediaExtractor;

import android.media.MediaFormat;

import com.sn.video.interfaces.IExtractor;

import java.nio.ByteBuffer;

public class AudioExtractor implements IExtractor {
    private MMExtractor mMediaExtractor;

    public AudioExtractor(String path) {
        mMediaExtractor = new MMExtractor(path);
    }


    @Override
    public MediaFormat getFormat() {
        return mMediaExtractor.getAudioFormat();
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
    public int getSampleFlag() {
        return mMediaExtractor.getSampleFlag();
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
