package com.xinruan.libs.camera.Camera2.GlsurfaceView;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.FloatMath;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CameraDrawer3 {

    private static final String TAG = "CameraDrawer";
    private final String VERTEX_SHADER = "" +
            "attribute vec4 vPosition;" +
            "attribute vec2 inputTextureCoordinate;" +
            "varying vec2 textureCoordinate;" +
            "void main()" +
            "{" +
            "gl_Position = vPosition;" +
            "textureCoordinate = inputTextureCoordinate;" +
            "}";
    private final String FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;" +
            "varying vec2 textureCoordinate;\n" +
            "uniform samplerExternalOES s_texture;\n" +
            "void main() {" +
            "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
            "}";

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mBackTextureBuffer;
    private FloatBuffer mFrontTextureBuffer;
    private ByteBuffer mDrawListBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mTextureHandle;


    private int SCREEN_WIDTH = 2244;

    private int SCREEN_HEIGHT = 1080;

//    // 由于相机自带旋转90度 所以纹理坐标数据可能需要旋转，顺时针
//    private static final float TEXTURE_BACK[] = {
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//            0.0f, 0.0f,
//            0.0f, 1.0f,
//    };

//    // 由于相机自带旋转90度 所以纹理坐标数据可能需要旋转，顺时针
//    private static final float TEXTURE_BACK[] = {
//
////            0, 0,
////            0, 1,
////            1, 1,
////            1, 0
//            0.5f, 0.5f,
//            1f, 0.5f,
//            0.5f, 0f,
//            0f, 0.5f,
//            0.5f, 1f,
//            1f, 0.5f,
//    };


    /**
     * 顶点贴图 :
     */
    private static final float VERTEXES[] = {
            0f, 0f,
            1, 0,
            0, 1f,
            -1f, 0f,
            0, -1f,
    };

//    private static final byte VERTEX_ORDER[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private final int VERTEX_SIZE = 2;
    private final int VERTEX_STRIDE = VERTEX_SIZE * 4;

    public CameraDrawer3() {
        // init float buffer for vertex coordinates
        // 将JAVA的 数组转换成ByteBUffer
        clacVerPoint();
        clacTexttrue();
        for (int i = 0; i < VERTEXES_RECT.length; i++) {
            Log.e(TAG, "VERTEXES_RECTA " + VERTEXES_RECT[i]);
        }
        mVertexBuffer = ByteBuffer.allocateDirect(VERTEXES_RECT.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(VERTEXES_RECT).position(0);

        // init float buffer for texture coordinates
        mBackTextureBuffer = ByteBuffer.allocateDirect(TEXTTURE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mBackTextureBuffer.put(TEXTTURE).position(0);

//        // init byte buffer for draw list
//        mDrawListBuffer = ByteBuffer.allocateDirect(VERTEX_ORDER.length).order(ByteOrder.nativeOrder());
//        mDrawListBuffer.put(VERTEX_ORDER).position(0);

        mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTextureHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
    }

    public void draw(int texture, boolean isFrontCamera) {
        GLES20.glUseProgram(mProgram); // 指定使用的program
        GLES20.glEnable(GLES20.GL_CULL_FACE); // 启动剔除
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture); // 绑定纹理
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mTextureHandle);
        GLES20.glVertexAttribPointer(mTextureHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mBackTextureBuffer);
        //        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_ORDER.length, GLES20.GL_UNSIGNED_BYTE, mDrawListBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, VERTEXES_RECT.length);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureHandle);
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        // 1. load shader
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == GLES20.GL_NONE) {
            Log.e(TAG, "load vertex shader failed! ");
            return GLES20.GL_NONE;
        }
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == GLES20.GL_NONE) {
            Log.e(TAG, "load fragment shader failed! ");
            return GLES20.GL_NONE;
        }
        // 2. create gl program
        int program = GLES20.glCreateProgram();
        if (program == GLES20.GL_NONE) {
            Log.e(TAG, "create program failed! ");
            return GLES20.GL_NONE;
        }
        // 3. attach shader
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        // we can delete shader after attach
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        // 4. link program
        GLES20.glLinkProgram(program);
        // 5. check link status
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == GLES20.GL_FALSE) { // link failed
            Log.e(TAG, "Error link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program); // delete program
            return GLES20.GL_NONE;
        }
        return program;
    }

    public static int loadShader(int type, String source) {
        // 1. create shader
        int shader = GLES20.glCreateShader(type);
        if (shader == GLES20.GL_NONE) {
            Log.e(TAG, "create shared failed! type: " + type);
            return GLES20.GL_NONE;
        }
        // 2. load shader source
        GLES20.glShaderSource(shader, source);
        // 3. compile shared source
        GLES20.glCompileShader(shader);
        // 4. check compile status
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == GLES20.GL_FALSE) { // compile failed
            Log.e(TAG, "Error compiling shader. type: " + type + ":");
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader); // delete shader
            shader = GLES20.GL_NONE;
        }
        return shader;
    }

    //    private float VERTEXES_RECT[] = new float[(36 / 4 + 1) * 2];
    private float VERTEXES_RECT[] = new float[92 * 2];
    private float TEXTTURE[] = new float[92 * 2];

    // 计算弧度
    //Math.PI * 2 = 360度
    //计算一弧度等于多少度
    float span = (float) (Math.PI * 2 / 360);

    public void clacVerPoint() {
        VERTEXES_RECT[0] = 0f;
        VERTEXES_RECT[1] = 0f;
        for (int i = 0; i < VERTEXES_RECT.length / 2 - 1; i++) {
//            Log.e(TAG, " i : " + i);
            float a = span * i;
            Log.e(TAG, "span " + a);
            VERTEXES_RECT[i * 2 + 2] = (float) (Math.cos(a) * 0.48);
            VERTEXES_RECT[i * 2 + 3] = (float) Math.sin(a);
        }
        for (int i = 0; i < VERTEXES_RECT.length; i++) {
            Log.e(TAG, "XY " + i + VERTEXES_RECT[i]);
        }
    }

    public void clacTexttrue() {
        TEXTTURE[0] = 0f;
        TEXTTURE[1] = 0f;
        int K = 0;
        for (int i = 2; i < TEXTTURE.length; i += 2) {
//            Log.e(TAG, " i : " + i);
            int i1 = K++;
            TEXTTURE[i] =  (float) Math.sin(span * i1);
            TEXTTURE[i + 1] =   (float) Math.cos(span * i1);
        }
//        reverse(TEXTTURE);
    }

    public static float[] reverse(float[] a) {
        float[] b = a;
        for (int start = 0, end = b.length - 1; start < end; start++, end--) {
            float temp = b[start];
            b[start] = b[end];
            b[end] = temp;
        }
        return b;
    }

    public float clacWitdhHeigtScale() {
        return (float) (SCREEN_HEIGHT / SCREEN_WIDTH);
    }
}
