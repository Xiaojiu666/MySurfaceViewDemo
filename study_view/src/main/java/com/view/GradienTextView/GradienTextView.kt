package com.view.GradienTextView

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.sn.study_pic.R

/**
 * Created by GuoXu on 2020/10/26 19:56.
 *
 */
class GradienTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    private var mOriginalColor = Color.BLACK
    private var mChangeColor = Color.RED
    private var mOriginalPaint: Paint? = null
    private var mChangePaint: Paint? = null
    private var orientation = Orientation.LEFT_TO_RIGHT
    private var baseLine = 0
    private var mCurrentProgress = 0f

    init {
        val td = context!!.obtainStyledAttributes(attrs, R.styleable.GradienTextView)
        mOriginalColor = td.getColor(R.styleable.GradienTextView_original_color, mOriginalColor)
        mChangeColor = td.getColor(R.styleable.GradienTextView_change_color, mChangeColor)
        td.recycle()
        //根据颜色获取画笔
        //根据颜色获取画笔
        mOriginalPaint = getPaintByColor(mOriginalColor)
        mChangePaint = getPaintByColor(mChangeColor)
    }

    private fun getPaintByColor(color: Int): Paint? {
        val paint = Paint()
        paint.color = color
        paint.isAntiAlias = true
        paint.isDither = true //防抖动
        paint.textSize = textSize
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        val middle = mCurrentProgress * width
        val fontMetricsInt = mOriginalPaint!!.fontMetricsInt
        baseLine = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom + height / 2 + paddingTop / 2 - paddingBottom / 2
        //前半部分
        Log.e("TAG", "orientation ${orientation}");
        if (orientation === Orientation.LEFT_TO_RIGHT) {
            clipRect(canvas, mCurrentProgress, middle, mChangePaint!!)
            clipRect(canvas, middle, width.toFloat(), mOriginalPaint!!)
        } else if (orientation === Orientation.INNER_TO_OUTER) {
            clipRect(canvas, width - middle, middle, mChangePaint!!)
            clipRect(canvas, middle, width - middle, mOriginalPaint!!)
        } else if (orientation === Orientation.RIGHT_TO_LEFT) {
            clipRect(canvas, width - middle, width.toFloat(), mChangePaint!!)
            clipRect(canvas, 0f, width - middle, mOriginalPaint!!)
        } else if (orientation === Orientation.RIGHT_TO_LEFT_FROM_NONE) {
            clipRect(canvas, width - middle, width.toFloat(), mChangePaint!!)
            clipRect(canvas, width.toFloat(), width - middle, mOriginalPaint!!)
        } else if (orientation === Orientation.LEFT_TO_RIGHT_FORME_NONE) {
            clipRect(canvas, 0f, middle, mChangePaint!!)
            clipRect(canvas, middle, 0f, mOriginalPaint!!)
        }
    }

    private fun clipRect(canvas: Canvas, start: Float, region: Float, paint: Paint) {
        //改变的颜色
        canvas.save()
        canvas.clipRect(start + paddingLeft, 0f, region, height.toFloat())
        canvas.drawText(text.toString(), paddingLeft.toFloat(), baseLine.toFloat(), paint)
        canvas.restore()
    }

    fun setOrientation(orientation: Orientation?) {
        this.orientation = orientation!!
    }

    fun setCurrentProgress(currentProgress: Float) {
        mCurrentProgress = currentProgress
        invalidate()
    }

    fun start(orientation: Orientation?, duration: Long) {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            setOrientation(orientation)
            setCurrentProgress(value)
        }
        animator.start()
    }
}