package com.view.study_opengles.first

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by GuoXu on 2020/10/28 19:44.
 *
 */
class SquareRender : GLSurfaceView.Renderer {

    private var vertexBuffer: FloatBuffer? = null
    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}"

    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"
    private var mProgram = 0
    var COORDS_PER_VERTEX = 2
    var triangleCoords = floatArrayOf(
            -1f, -1f,  // top left
            -1f, 1f,   // bottom left
            1f, -1f,  // bottom right
            1f, 1f // top right
    )

    private var mPositionHandle = 0
    private var mColorHandle = 0
    //顶点个数
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    //顶点之间的偏移量
    private val vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
    var color = floatArrayOf(1.0f, 0.5f, 0.2f, 1.0f)
    override fun onDrawFrame(p0: GL10?) {
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
        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
//        https://blog.csdn.net/sinat_29255093/article/details/103297500
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height);
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        //将背景设置为灰色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        val bb: ByteBuffer = ByteBuffer.allocateDirect(
                triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(triangleCoords)
        vertexBuffer?.position(0)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode)
        //创建一个空的OpenGLES程序
        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram()
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader)
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader)
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram)
    }

    /**
     * 装载着色器
     *
     * @param type       着色器类型
     * @param shaderCode 着色器代码
     * @return
     */
    fun loadShader(type: Int, shaderCode: String?): Int {
        // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
        // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
        val shader = GLES20.glCreateShader(type)
        // 添加上面编写的着色器代码并编译它
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}