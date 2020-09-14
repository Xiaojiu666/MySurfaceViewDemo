package com.sn.study_opengles.graphical;

import android.opengl.GLES20;

import com.sn.study_opengles.BaseRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by GuoXu on 2020/9/14 11:16.
 */
public class BaseTriangle {

    /**
     * 顶点着色程序 - 用于渲染形状的顶点的 OpenGL ES 图形代码。
     */
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
                    "void main() {" +
//                    "  gl_Position = vPosition;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    /**
     * 片段着色程序 - 用于使用颜色或纹理渲染形状面的 OpenGL ES 代码。
     */
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private FloatBuffer vertexBuffer;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    // 顶点坐标系是-1f,可以超出 有效果
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 1f, 0.0f, // top
            -1f, 0f, 0.0f,// bottom left
            1f, 0f, 0.0f, // bottom right
    };


    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};


    private final int mProgram;
    private int vPMatrixHandle;

    public BaseTriangle() {
        /**
         * 数组转换Buffer 转为GL 所需要数据
         */
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


        /**
         * 根据类型和代码，为GL加载 顶点&片段着色器对象
         */
        int vertexShader = BaseRender.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = BaseRender.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();
        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }


    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        //获取顶点着色器的vPosition成员的句柄
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用三角形顶点的控制柄
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 准备三角坐标数据
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);


        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 禁用顶点阵列
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
