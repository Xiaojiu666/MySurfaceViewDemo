package com.xinruan.libs.camera.Camera2.SurfaceView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;

/**
 * Created by GuoXu on 2020/9/6 16:33.
 */
public class AutoFitSurfaceView extends SurfaceView {
    private static final String TAG = "AutoFitTextureView";
    private int ratioWidth = 0;
    private int ratioHeight = 0;

    public AutoFitSurfaceView(final Context context) {
        this(context, null);
    }

    public AutoFitSurfaceView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitSurfaceView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置一个宽高，用于绘制 Holder的宽高，同时根据比例绘制当前View的 尺寸
     * 当前 View的尺寸比例一定要满足相机支持的尺寸比，否则，预览画面将会异常
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(final int width, final int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        ratioWidth = width;
        ratioHeight = height;
        getHolder().setFixedSize(width, height);
        requestLayout();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == ratioWidth || 0 == ratioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * ratioWidth / ratioHeight) {
                Log.e(TAG, "onMeasure  ratioWidth" + ratioWidth + " ratioHeight" + ratioHeight);
                setMeasuredDimension(width, width * ratioHeight / ratioWidth);
                Log.e(TAG, "onMeasure  width" + width + " width * ratioHeight / ratioWidth" + width * ratioHeight / ratioWidth);
            } else {
                setMeasuredDimension(height * ratioWidth / ratioHeight, height);
                Log.e(TAG, "onMeasure  height * ratioWidth / ratioHeight" + height * ratioWidth / ratioHeight + "height" + height);
            }
        }

    }
}
