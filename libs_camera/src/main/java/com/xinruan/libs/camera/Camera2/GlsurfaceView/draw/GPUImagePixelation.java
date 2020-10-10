package com.xinruan.libs.camera.Camera2.GlsurfaceView.draw;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import com.xinruan.libs.camera.Camera2.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by GuoXu on 2020/9/14 19:32.
 */
public class GPUImagePixelation {

    private static final String TAG = "CameraDrawer";
    private final String VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";
    private final String FRAGMENT_SHADER = "" +
            "precision highp float;\n" +

            "varying vec2 textureCoordinate;\n" +

            "uniform float imageWidthFactor;\n" +
            "uniform float imageHeightFactor;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "uniform float pixel;\n" +

            "void main()\n" +
            "{\n" +
            "  vec2 uv  = textureCoordinate.xy;\n" +
            "  float dx = pixel * imageWidthFactor;\n" +
            "  float dy = pixel * imageHeightFactor;\n" +
            "  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n" +
            "  vec3 tc = texture2D(inputImageTexture, coord).xyz;\n" +
            "  gl_FragColor = vec4(tc, 1.0);\n" +
            "}";
    private final int glUniformTexture;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mBackTextureBuffer;
    private FloatBuffer mFrontTextureBuffer;
    private ByteBuffer mDrawListBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mTextureHandle;
    private float pixel = 1.0f;

    /**
     * 顶点贴图 : 用于相机数据绘制
     * 顶点和纹理坐标 数据进行对应 个数必须相等
     */
    private static final float VERTEXES[] = {
            -1.0f, 1.0f,
            -1.0f, 0f,
            -1.0f, -1.0f,
            0f, -1.0f,
            0f, 0.5f,
            0f, 1.0f,
    };

    // 后置摄像头使用的纹理坐标
//    private static final float TEXTURE_BACK[] = {
//            0.0f, 1.0f,
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//            0.0f, 0.0f,
//    };
    private static final float TEXTURE_BACK[] = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
    };

    // 由于相机自带旋转90度 所以纹理坐标数据可能需要旋转，顺时针
//    private static final float TEXTURE_BACK[] = {
//            0.0f, 1.0f,
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//            0.0f, 0.0f,
//    };


    // 前置摄像头使用的纹理坐标
    private static final float TEXTURE_FRONT[] = {
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    private static final byte VERTEX_ORDER[] = {0, 1, 2, 3}; // order to draw vertices

    private final int VERTEX_SIZE = 2;
    private final int VERTEX_STRIDE = VERTEX_SIZE * 4;
    private int imageWidthFactorLocation;
    private int imageHeightFactorLocation;
    private int pixelLocation;

    public GPUImagePixelation() {
        // init float buffer for vertex coordinates
        // 将JAVA的 数组转换成ByteBUffer
        mVertexBuffer = ByteBuffer.allocateDirect(VERTEXES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(VERTEXES).position(0);

        // init float buffer for texture coordinates
        mBackTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_BACK.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mBackTextureBuffer.put(TEXTURE_BACK).position(0);
        mFrontTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_FRONT.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mFrontTextureBuffer.put(TEXTURE_FRONT).position(0);

        // init byte buffer for draw list
        mDrawListBuffer = ByteBuffer.allocateDirect(VERTEX_ORDER.length).order(ByteOrder.nativeOrder());
        mDrawListBuffer.put(VERTEX_ORDER).position(0);

        mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        glUniformTexture = GLES20.glGetUniformLocation(mProgram, "inputImageTexture");
        mTextureHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        imageWidthFactorLocation = GLES20.glGetUniformLocation(mProgram, "imageWidthFactor");
        imageHeightFactorLocation = GLES20.glGetUniformLocation(mProgram, "imageHeightFactor");
        pixelLocation = GLES20.glGetUniformLocation(mProgram, "pixel");
    }

    public void onOutputSizeChanged(final int width, final int height) {
        setFloat(imageWidthFactorLocation, 1.0f / width);
        setFloat(imageHeightFactorLocation, 1.0f / height);
    }

    protected void setFloat(final int location, final float floatValue) {
        GLES20.glUniform1f(location, floatValue);
    }

    public void draw(int texture) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glUseProgram(mProgram); // 指定使用的program
        GLES20.glEnable(GLES20.GL_CULL_FACE); // 启动剔除
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture); // 绑定纹理
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureHandle);
        GLES20.glVertexAttribPointer(mTextureHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mBackTextureBuffer);

        // 真正绘制的操作
//        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, VERTEX_ORDER.length, GLES20.GL_UNSIGNED_BYTE, mDrawListBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
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
}
