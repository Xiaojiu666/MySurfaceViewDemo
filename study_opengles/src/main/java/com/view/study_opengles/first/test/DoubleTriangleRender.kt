package com.view.study_opengles.first.test

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.view.study_opengles.BufferFloatUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by GuoXu on 2020/10/26 9:55.
 *
 */
class DoubleTriangleRender : GLSurfaceView.Renderer {


    // 1.顶点着色器
    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}"
    private var indexBuffer: ShortBuffer? = null
    // 1. 顶点数组
    var vertices = floatArrayOf(
            // first triangle
            -0.9f, -0.5f, 0.0f,  // left
            -0.0f, -0.5f, 0.0f,  // right
            -0.45f, 0.5f, 0.0f,  // top
            // second triangle
            0.0f, -0.5f, 0.0f,  // left
            0.9f, -0.5f, 0.0f,  // right
            0.45f, 0.5f, 0.0f   // top
    )


    var index = shortArrayOf(
            0, 1, 2, 3, 4, 5
    )
    /**
     * 片段着色程序 - 用于使用颜色或纹理渲染形状面的 OpenGL ES 代码。
     */
    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"

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

    override fun onDrawFrame(p0: GL10?) {
        //将程序加入到OpenGLES2.0环境
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)
        //获取顶点着色器的vPosition成员句柄
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点的句柄
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //准备三角形的坐标数据
        //链接顶点属性
        //https://www.cnblogs.com/salam/p/4970418.html
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, BufferFloatUtil.float2Buffer(vertices))
        //获取片元着色器的vColor成员的句柄


        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        //设置绘制三角形的颜色
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        //绘制三角形
        //绘制三角形
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)
        //禁止顶点数组的句柄
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        //将背景设置为灰色
    }




    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        GLES20.glViewport(0, 0, p1, p2);
    }

    private var mPositionHandle = 0
    private var mProgram = 0
    private val COORDS_PER_VERTEX = 3
    private var mColorHandle = 0
    var color = floatArrayOf(1.0f, 0.5f, 0.2f, 1f) //白色
    private val vertexCount: Int = vertices.size / COORDS_PER_VERTEX

    //顶点之间的偏移量
    private val vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        //将背景设置为灰色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        BufferFloatUtil.float2Buffer(vertices)
        val cc: ByteBuffer = ByteBuffer.allocateDirect(index.size * 2)
        cc.order(ByteOrder.nativeOrder())
        indexBuffer = cc.asShortBuffer()
        indexBuffer?.put(index)
        indexBuffer?.position(0)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode)
        // 创建空的OpenGL ES程序
        mProgram = GLES20.glCreateProgram()
        // 添加顶点着色器到程序中
        GLES20.glAttachShader(mProgram, vertexShader)
        // 添加片段着色器到程序中
        GLES20.glAttachShader(mProgram, fragmentShader)
        // 创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(mProgram)

    }
}