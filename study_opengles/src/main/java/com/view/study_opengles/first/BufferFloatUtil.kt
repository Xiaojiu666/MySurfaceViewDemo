package com.view.study_opengles.first

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by GuoXu on 2020/10/26 11:33.
 *
 */
object BufferFloatUtil {

    /**
     * 数组转换Buffer 转为GL 所需要数据
     */
    fun float2Buffer(float: FloatArray): FloatBuffer {

        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect( // (number of coordinate values * 4 bytes per float)
                float.size * 4)
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())
        // create a floating point buffer from the ByteBuffer
        var vertexBuffer = bb.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(float)
        // set the buffer to read the first coordinate
        vertexBuffer.position(0)
        return vertexBuffer;
    }
}