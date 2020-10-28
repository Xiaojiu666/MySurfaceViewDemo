package com.view.study_opengles

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log

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
        setRenderer(BaseRender())
//        该设置可防止系统在您调用 requestRender() 之前重新绘制 GLSurfaceView 帧，这对于此示例应用而言更为高效。
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        print("onMeasure " + width);
        Log.e("onMeasure ", width.toString());
        // 根据手机屏幕宽高设置框预览比例4:3
//        setMeasuredDimension(640, 640);
    }


}