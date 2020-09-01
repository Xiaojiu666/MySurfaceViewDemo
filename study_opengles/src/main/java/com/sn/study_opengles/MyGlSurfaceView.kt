package com.sn.study_opengles

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by GuoXu on 2020/8/23 12:44.
 *
 */
class MyGlSurfaceView : GLSurfaceView {

    constructor(ctx: Context) : this(ctx, null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs)

    //初始化用
    //先执行init模块逻辑，后执行构造方法的逻辑
    init {
        setEGLContextClientVersion(2)
        setRenderer(MyRender1())
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY)
    }



}