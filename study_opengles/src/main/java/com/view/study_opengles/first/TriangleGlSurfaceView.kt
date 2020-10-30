package com.view.study_opengles.first

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by GuoXu on 2020/10/26 9:53.
 * GLStudy
 */
class TriangleGlSurfaceView : GLSurfaceView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        setEGLContextClientVersion(2)
        setRenderer(SquareRender())
    }
}