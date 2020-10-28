package com.view.cornerImage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

/**
 * Created by GuoXu on 2020/8/25 9:55.
 */
class CornerImage @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : ImageView(context, attrs, defStyleAttr) {


    var paint: Paint = Paint();
    var rectF: RectF? = null;
    override fun onDraw(canvas: Canvas) {
        Log.i("onDraw", rectF.toString())
        canvas.drawRoundRect(rectF, 16f, 16f, paint);
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat());
        Log.i("onLayout", left.toString() + " " + top + " " + right + " " + bottom)
    }

    init {
        initPaint()
    }

    private fun initPaint() {
        paint.color = Color.BLACK;
        paint.strokeWidth = 10f;
    }
}