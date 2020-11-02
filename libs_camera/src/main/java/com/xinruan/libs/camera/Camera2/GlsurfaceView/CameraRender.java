package com.xinruan.libs.camera.Camera2.GlsurfaceView;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.xinruan.libs.camera.Camera2.Camera2Manager;
import com.xinruan.libs.camera.Camera2.GlsurfaceView.draw.GPUImagePixelation;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by GuoXu on 2020/9/9 17:07.
 */
public class CameraRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CameraRender";
    public Activity mContext;
    public GLSurfaceView glView;
    private CameraDrawer mDrawer;
    private int[] texture;
    private CameraDrawer1 mDrawer1;
    private CameraDrawer2 mDrawer2;

    public CameraRender(Activity mContext, GLSurfaceView glView) {
        this.mContext = mContext;
        this.glView = glView;
    }

    private SurfaceTexture mSurfaceTexture;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG,"onSurfaceCreated");
        texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        // OES纹理坐标配置 绘制YUV格式图片
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        mDrawer = new CameraDrawer();
        mDrawer1 = new CameraDrawer1();
        mDrawer2 = new CameraDrawer2();
        mSurfaceTexture = new SurfaceTexture(texture[0]);
        mSurfaceTexture.setDefaultBufferSize(4000, 3000);
        //监听有新图像到来
        mSurfaceTexture.setOnFrameAvailableListener(this);
        Camera2Manager camera2Manager = new Camera2Manager(mContext);
        camera2Manager.setCameraId("0");
        camera2Manager.setPreviewSurface(mSurfaceTexture);
        camera2Manager.openCamera();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG,"onSurfaceChanged");
//        mDrawer2.onOutputSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG,"onDrawFrame");
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mDrawer1.draw(texture[0], false);
        mDrawer2.draw(texture[0], false);
//        mDrawer1.draw(texture[0], false);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.e(TAG,"onFrameAvailable");
        glView.requestRender();
    }
}
