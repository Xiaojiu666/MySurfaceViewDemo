package com.sn.study_opengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.sn.study_opengles.graphical.Square;
import com.sn.study_opengles.graphical.Triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by GuoXu on 2020/8/23 14:22.
 */
public class MyRender1 implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square mSquare;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //将背景设置为灰色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // 初始化triangle
        mTriangle = new Triangle();
        // 初始化 square
        mSquare = new Square();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        // Redraw background color
        GLES20.glViewport(0, 0, i, i1);

    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }
}
