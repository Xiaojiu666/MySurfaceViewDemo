package com.view.study_opengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by GuoXu on 2020/8/23 14:22.
 * https://blog.csdn.net/hjj378315764/article/details/79701040
 */
public class MyRender implements GLSurfaceView.Renderer {
    private static final String TAG = "MyRender";
    private String vertexShaderCode = " attribute vec4 vPosition;\n" +
            " void main() {\n" +
            "     gl_Position = vPosition;\n" +
            " }";
    private String fragmentShaderCode = "precision mediump float;\n" +
            " uniform vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    float triangleCoords[] = {
            0f, 0.5f, 0.F,// top
            -0.5f, -0.5f, 0.F, // bottom left
            0.5f, -0.5f, 0.F, // bottom right
    };
    float color[] = {1.0f, 0f, 0f, 1f}; //白色
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //将背景设置为灰色
        GLES20.glClearColor(1f, 0f, 0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //申请底层空间B
        // yteBuffer提供了两种静态实例方式：
        //https://blog.csdn.net/seebetpro/article/details/49184305
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);
        Log.e(TAG, bb.toString());
        //返回ByteBuffer的字节序。
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    public int loadShader(int type, String shaderCode) {
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }

    static final int COORDS_PER_VERTEX = 3;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节

    @Override
    public void onDrawFrame(GL10 gl10) {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
