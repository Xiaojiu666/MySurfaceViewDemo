package com.view.study_opengles.first

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.view.study_opengles.BaseRender

/**
 * Created by GuoXu on 2020/10/26 9:53.
 * GLStudy
 */
class TriangleGlSurfaceView : GLSurfaceView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    var baseRender: BaseRender

    init {
        setEGLContextClientVersion(2)
        baseRender = BaseRender()
        setRenderer(baseRender)
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY)
//        requestRender()
    }

    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    private var previousX = 0f
    private var previousY = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        val x: Float = e.getX()
        val y: Float = e.getY()
        when (e.getAction()) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - previousX
                var dy = y - previousY
                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx = dx * -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy = dy * -1
                }
                baseRender?.angle = baseRender?.angle +
                        (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }
        previousX = x
        previousY = y
        return true
    }
}