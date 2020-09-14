package com.sn.study_opengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.sn.study_opengles.graphical.BaseSquare;
import com.sn.study_opengles.graphical.BaseTriangle;
import com.sn.study_opengles.graphical.Square;
import com.sn.study_opengles.graphical.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by GuoXu on 2020/9/14 10:09.
 */
public class BaseRender implements GLSurfaceView.Renderer {
    private static final String TAG = "BaseRender";
    private BaseTriangle mTriangle;
    private BaseSquare mSquare;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 0.5f);
        // initialize a triangle
        mTriangle = new BaseTriangle();
        // initialize a square
        mSquare = new BaseSquare();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.e(TAG, "width " + i + "height " + i1);
        GLES20.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }

    /**
     * 着色程序包含 OpenGL 着色语言 (GLSL) 代码，必须先对其进行编译，
     * 然后才能在 OpenGL ES 环境中使用。要编译此代码，请在您的渲染程序类中创建一个实用程序方法：
     *
     * @param type       类型 ：
     * @param shaderCode 代码 ：
     * @return
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
