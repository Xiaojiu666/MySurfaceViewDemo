package com.sn.libs_video;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sn.libs_video.interfaces.IDecoder;
import com.sn.libs_video.interfaces.IDecoderStateListener;
import com.sn.libs_video.interfaces.IExtractor;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;


/**
 * 定义解码器
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public abstract class BaseDecoder implements IDecoder {


    BaseDecoder(String mFilePath) {
        this.mFilePath = mFilePath;
    }


    public static final String TAG = "BaseDecoder";

    //-------------线程相关------------------------
    /**
     * 解码器是否在运行
     */
    private boolean mIsRunning = true;

    /**
     * 线程等待锁
     */
    private Object mLock = new Object();

    /**
     * 是否可以进入解码
     */
    private boolean mReadyForDecode = false;

    //---------------解码相关-----------------------
    /**
     * 音视频解码器
     */
    protected MediaCodec mCodec;

    /**
     * 音视频数据读取器
     */
    protected IExtractor mExtractor;

    /**
     * 解码输入缓存区
     */
    protected ArrayList<ByteBuffer> mInputBuffers;

    /**
     * 解码输出缓存区
     */
    protected ArrayList<ByteBuffer> mOutputBuffers;

    /**
     * 解码数据信息
     */
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private DecodeState mState = DecodeState.STOP;

    private IDecoderStateListener mStateListener;

    /**
     * 流数据是否结束
     */
    private boolean mIsEOS = false;

    protected int mVideoWidth = 0;

    protected int mVideoHeight = 0;

    @Override
    public void pause() {
    }

    @Override
    public void goOn() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isDecoding() {
        return false;
    }

    @Override
    public boolean isSeeking() {
        return false;
    }

    @Override
    public boolean isStop() {
        return false;
    }

    @Override
    public void setStateListener() {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public int getRotationAngle() {
        return 0;
    }

    @Override
    public MediaFormat getMediaFormat() {
        return null;
    }

    @Override
    public int getTrack() {
        return 0;
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public void run() {
        mState = DecodeState.START;
        mStateListener.decoderPrepare(this);
        //【解码步骤：1. 初始化，并启动解码器】
        if (!init()) return;

        while (mIsRunning) {
            if (mState != DecodeState.START &&
                    mState != DecodeState.DECODING &&
                    mState != DecodeState.SEEKING) {
                waitDecode();
            }

            if (!mIsRunning ||
                    mState == DecodeState.STOP) {
                mIsRunning = false;
                break;
            }

            //如果数据没有解码完毕，将数据推入解码器解码
            if (!mIsEOS) {
                //【解码步骤：2. 将数据压入解码器输入缓冲】
                mIsEOS = pushBufferToDecoder();
            }

            //【解码步骤：3. 将解码好的数据从缓冲区拉取出来】
            int index = pullBufferFromDecoder();
            if (index >= 0) {
                //【解码步骤：4. 渲染】
                render(mOutputBuffers !![index],mBufferInfo);
                //【解码步骤：5. 释放输出缓冲】
                mCodec !!.releaseOutputBuffer(index, true)
                if (mState == DecodeState.START) {
                    mState = DecodeState.PAUSE
                }
            }
            //【解码步骤：6. 判断解码是否完成】
            if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                mState = DecodeState.FINISH
                mStateListener ?.decoderFinish(this)
            }
        }
        doneDecode();
        //【解码步骤：7. 释放解码器】
        release();
    }


    private String mFilePath;

    private boolean init() {
        //1.检查参数是否完整
        if (mFilePath.isEmpty() || new File(mFilePath).exists()) {
            Log.w(TAG, "文件路径为空");
            mStateListener.decoderError(this, "文件路径为空");
            return false;
        }
        //调用虚函数，检查子类参数是否完整
        if (!check()) return false;

        //2.初始化数据提取器
        mExtractor = initExtractor(mFilePath);
        if (mExtractor == null ||
                mExtractor.getFormat() == null) return false;

        //3.初始化参数
        if (!initParams()) return false;

        //4.初始化渲染器
        if (!initRender()) return false;

        //5.初始化解码器
        if (!initCodec()) return false;
        return true;
    }

    private boolean initParams() {
        try {
            MediaFormat format = mExtractor.getFormat();
            long mDuration = format.getLong(MediaFormat.KEY_DURATION) / 1000;
            if (mEndPos == 0L)
                mEndPos = mDuration;

            initSpecParams(mExtractor.getFormat());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 检查子类参数
     */
    abstract boolean check();

    /**
     * 初始化数据提取器
     */
    abstract IExtractor initExtractor(IExtractor path);

    /**
     * 初始化子类自己特有的参数
     */
    abstract void initSpecParams(MediaFormat format);

    /**
     * 初始化渲染器
     */
    abstract boolean initRender();

    /**
     * 配置解码器
     */
    abstract boolean configCodec(MediaCodec codec, MediaFormat format);
}
