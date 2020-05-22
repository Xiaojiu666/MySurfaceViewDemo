package com.sn.libs_video.interfaces;

import com.sn.libs_video.BaseDecoder;
import com.sn.libs_video.Frame;

public interface IDecoderStateListener {

    void decoderPrepare(BaseDecoder decodeJob);

    void decoderReady(BaseDecoder decodeJob);

    void decoderRunning(BaseDecoder decodeJob);

    void decoderPause(BaseDecoder decodeJob);

    void decodeOneFrame(BaseDecoder decodeJob, Frame frame);

    void decoderFinish(BaseDecoder decodeJob);

    void decoderDestroy(BaseDecoder decodeJob);

    void decoderError(BaseDecoder decodeJob, String msg);
}
