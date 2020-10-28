package com.view.EyesPointView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast

/**
 * Created by GuoXu on 2020/10/27 11:50.
 */
class EyesPointView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {


    companion object {
        /*默认瞳距*/
        const val DEFAULT_PUPIL_DISTANCE = 2792 / 2

        /*默认view 宽高*/
        const val DEFAULT_VIEW_HEIGHT = 1080
        const val DEFAULT_VIEW_WIDTH = 1920

        /*默认中心十字 宽高*/
        const val DEFAULT_X_WIDTH = 100
        const val DEFAULT_X_HEIGHT = 100

        /*默认矩阵宽高(正方形)*/
        const val DEFAULT_RECT_WIDTH = 800

        /*默认原型半径*/
        const val DEFAULT_CIRCLE_RADIUS = 5f

        /*远点矩阵宽高*/
        const val DEFAULT_MATRIX_ROW_SIZE = 33
        const val DEFAULT_MATRIX_COLUMN_SIZE = 33

        private const val TAG = "EyesPointView"
    }

    /**
     * 瞳距
     */
    var distancePupil = DEFAULT_PUPIL_DISTANCE

    private var viewWidth = 0
    private var viewHeight = 0
    private var viewRadiusWidth = 0
    private var viewRadiusHeight = 0
    private var xWidth = DEFAULT_X_WIDTH
    private var xHeight = DEFAULT_X_HEIGHT
    private var rectWidth = DEFAULT_RECT_WIDTH
    var pointLocation = Point(0, 0)

    var circleLeftPoints: ArrayList<CirclePoint>? = ArrayList()
    var circleRightPoints: ArrayList<CirclePoint>? = ArrayList()
    private var paint: Paint? = null
    private var paint1: Paint? = null
    private var paint2: Paint? = null
    private var paint3: Paint? = null
    private var onCircleDrawComplete: OnCircleDrawComplete? = null

    private fun initPaint() {
        paint = Paint()
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.GREEN
        paint!!.strokeWidth = 10f
        paint!!.isAntiAlias = true //取消锯齿

        paint1 = Paint()
        paint1!!.style = Paint.Style.FILL_AND_STROKE
        paint1!!.color = Color.WHITE
        paint1!!.isAntiAlias = true //取消锯齿

        paint2 = Paint()
        paint2!!.style = Paint.Style.FILL_AND_STROKE
        paint2!!.color = Color.RED
        paint2!!.isAntiAlias = true //取消锯齿

        paint3 = Paint()
        paint3!!.style = Paint.Style.FILL_AND_STROKE
        paint3!!.alpha = 255
        paint3!!.color = Color.RED
        paint3!!.isAntiAlias = true //取消锯齿
    }

    /**
     * 测量view 宽高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取测量模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取测量大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            // match_parent & DP
            viewWidth = widthSize
            viewHeight = heightSize
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            // warp_content
            viewWidth = DEFAULT_VIEW_WIDTH
            viewHeight = DEFAULT_VIEW_HEIGHT
        }
        viewRadiusWidth = viewWidth.div(2)
        viewRadiusHeight = viewHeight.div(2)
        setMeasuredDimension(viewWidth, viewHeight)
        onMeasureCircleLocation()
    }

    private fun onMeasureCircleLocation() {
        circleLeftPoints?.clear()
        circleRightPoints?.clear()
        val drawLeftRect = getRect4Point(getLeftEyePoint())
        val drawRightRect = getRect4Point(getRightEyePoint())
        Log.e(TAG, "drawLeftRect : ${drawLeftRect} drawRightRect : $drawRightRect")
        getCircleCircleMatrix(drawLeftRect, true)
        getCircleCircleMatrix(drawRightRect, false)
    }


    /**
     * 绘制View
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e(TAG, "viewWidth : $viewWidth, viewHeight : $viewHeight");
        /* 中线*/
        canvas.drawLine(viewWidth / 2.toFloat(), 0f, viewWidth / 2.toFloat(), viewHeight.toFloat(), paint)
        /* 左右眼十字*/
        drawLeftX(canvas)
        drawRightX(canvas)
        onMeasureCircleLocation()
//        drawAllCircle4Point(canvas)
        /*绘制选中圆点*/
        Log.e(TAG, "onDraw isDrawComplete $isDrawComplete")
        if (!isDrawComplete) {
            drawSelectorCircle4Point(canvas)
        }
    }


    private fun drawLeftX(canvas: Canvas) {
        val leftEyePoint = getLeftEyePoint()
        val startX = leftEyePoint.x - xWidth.div(2).toFloat()
        val startY = leftEyePoint.y.toFloat()
        val stopX = leftEyePoint.x + xWidth.div(2).toFloat()
        val stopY = leftEyePoint.y.toFloat()
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        val startXY = leftEyePoint.x.toFloat()
        val startYY = leftEyePoint.y - xHeight.div(2).toFloat()
        val stopXY = leftEyePoint.x.toFloat()
        val stopYY = leftEyePoint.y + xHeight.div(2).toFloat()
        Log.e(TAG, "startXY : $startXY，startXY : $startYY，startXY : $stopXY，startXY : $stopYY")
        canvas.drawLine(startXY, startYY, stopXY, stopYY, paint)
    }

    /**
     * 获取左眼 十字坐标点
     */
    private fun getLeftEyePoint(): Point {
        /*左眼十字中心点*/
        val pointCenterX = viewRadiusWidth - distancePupil.div(2)
        val pointCenterY = viewRadiusHeight
        return Point(pointCenterX, pointCenterY)
    }

    /**
     * 获取右眼 十字坐标点
     */
    private fun getRightEyePoint(): Point {
        /*右眼十字中心点*/
        val pointCenterX = viewRadiusWidth + distancePupil.div(2)
        val pointCenterY = viewRadiusHeight
        return Point(pointCenterX, pointCenterY)
    }

    private fun drawRightX(canvas: Canvas) {
        val rightEyePoint = getRightEyePoint()
        val startX = rightEyePoint.x - xWidth.div(2).toFloat()
        val startY = rightEyePoint.y.toFloat()
        val stopX = rightEyePoint.x + xWidth.div(2).toFloat()
        val stopY = rightEyePoint.y.toFloat()
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        val startXY = rightEyePoint.x.toFloat()
        val startYY = rightEyePoint.y - xHeight.div(2).toFloat()
        val stopXY = rightEyePoint.x.toFloat()
        val stopYY = rightEyePoint.y + xHeight.div(2).toFloat()
        Log.e(TAG, "startXY : $startXY，startXY : $startYY，startXY : $stopXY，startXY : $stopYY")
        canvas.drawLine(startXY, startYY, stopXY, stopYY, paint)
    }

    private fun getRect4Point(leftPoint: Point): RectF {
        val rectRadius = rectWidth / 2
        val rectF = RectF(leftPoint.x - rectRadius.toFloat(), leftPoint.y - rectRadius.toFloat(), leftPoint.x + rectRadius.toFloat(), leftPoint.y + rectRadius.toFloat());
        return rectF;
    }

    private fun drawAllCircle4Point(canvas: Canvas) {
        Log.e(TAG, "point $pointLocation")
        for (left in 0 until circleLeftPoints!!.size) {
            val leftCircle = circleLeftPoints?.get(left)
            val rightCircle = circleRightPoints?.get(left)
            canvas.drawCircle(leftCircle!!.x, leftCircle.y, DEFAULT_CIRCLE_RADIUS, paint1!!)
            canvas.drawCircle(rightCircle!!.x, rightCircle.y, DEFAULT_CIRCLE_RADIUS, paint1!!)
        }
    }


    var isDrawComplete = false
    private fun drawSelectorCircle4Point(canvas: Canvas) {
        Log.e(TAG, "point $pointLocation")
        val posi = pointLocation.x * DEFAULT_MATRIX_ROW_SIZE + pointLocation.y
        val leftCircle = circleLeftPoints?.get(posi)
        val rightCircle = circleRightPoints?.get(posi)
        canvas.drawCircle(leftCircle!!.x, leftCircle.y, DEFAULT_CIRCLE_RADIUS, paint2!!)
        canvas.drawCircle(rightCircle!!.x, rightCircle.y, DEFAULT_CIRCLE_RADIUS, paint2!!)
        postInvalidateDelayed(2000)
        isDrawComplete = true;
    }

    private fun getCircleCircleMatrix(rectF: RectF, isLeft: Boolean) {

        val rectWidth = rectF.right - rectF.left;
        val rectHeight = rectF.bottom - rectF.top;
        //列宽
        val columnWidth = rectWidth / (DEFAULT_MATRIX_COLUMN_SIZE - 1);
        //行高
        val rowHeight = rectHeight / (DEFAULT_MATRIX_ROW_SIZE - 1);
        for (row in 0 until DEFAULT_MATRIX_ROW_SIZE) {
            for (column in 0 until DEFAULT_MATRIX_COLUMN_SIZE) {
                //canvas.drawCircle(rectF.left + columnWidth * column, rectF.top + rowHeight * row, DEFAULT_CIRCLE_RADIUS, paint1)
                if (isLeft) {
                    val point = CirclePoint(rectF.left + columnWidth * column, rectF.top + rowHeight * row)
                    circleLeftPoints?.add(point);
                } else {
                    val point = CirclePoint(rectF.left + columnWidth * column, rectF.top + rowHeight * row)
                    circleRightPoints?.add(point);
                }
            }
        }
        if (onCircleDrawComplete != null && !isLeft) {
            onCircleDrawComplete?.onDrawComplete()
        }
        Log.e(TAG, "leftCircles ${circleLeftPoints}")
        Log.e(TAG, "rightCircles $circleRightPoints}")
    }

    init {
        initPaint()
    }


    class CirclePoint(val x: Float, val y: Float) {
        override fun toString(): String {
            return "{x : $x ,y : $y}"
        }
    }

    fun setOnCircleDrawComplete(onCircleDrawComplete: OnCircleDrawComplete?) {
        this.onCircleDrawComplete = onCircleDrawComplete
    }

    interface OnCircleDrawComplete {
        fun onDrawComplete();
    }

    fun moveToTop(setup: Int) {
        var newPoint = Point(pointLocation.x - setup, pointLocation.y);
        if (checkLocationValid(newPoint)) {
            pointLocation = newPoint;
            viewInvalidate()
        }
    }

    private fun viewInvalidate() {
        isDrawComplete = false
        invalidate()
    }

    fun moveToBottom(setup: Int) {
        var newPoint = Point(pointLocation.x + setup, pointLocation.y);
        if (checkLocationValid(newPoint)) {
            pointLocation = newPoint;
            viewInvalidate()
        }
    }

    fun moveToLeft(setup: Int) {
        var newPoint = Point(pointLocation.x, pointLocation.y - setup);
        if (checkLocationValid(newPoint)) {
            pointLocation = newPoint;
            viewInvalidate()
        }
    }

    fun moveToRight(setup: Int) {
        var newPoint = Point(pointLocation.x, pointLocation.y + setup);
        if (checkLocationValid(newPoint)) {
            pointLocation = newPoint;
            viewInvalidate()
        }
    }

    private fun checkLocationValid(pointLocation: Point): Boolean {
        if (pointLocation.x in 0 until EyesPointView.DEFAULT_MATRIX_ROW_SIZE && pointLocation.y in 0 until DEFAULT_MATRIX_COLUMN_SIZE) {
            return true
        }
        return false
    }
}