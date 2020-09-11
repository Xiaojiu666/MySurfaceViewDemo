package com.xinruan.libs.camera.Camera2.GlsurfaceView;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by GuoXu on 2020/9/9 17:03.
 */
public class AutoFitGlView extends GLSurfaceView {
    public AutoFitGlView(Context context) {
        this(context, null);
    }

    public AutoFitGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置EGL 版本
        setEGLContextClientVersion(2);
        CameraRender glRender = new CameraRender((Activity) context,this);
        setRenderer(glRender);
        //手动渲染模式
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
