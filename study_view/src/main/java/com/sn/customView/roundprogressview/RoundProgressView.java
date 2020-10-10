package com.sn.customView.roundprogressview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import androidx.annotation.IntRange;

import com.sn.study_pic.R;

import static android.graphics.Paint.Style.STROKE;

/**
 * @author cici$
 * @description 圆形进度条$
 * @time 2020.10.10$ $
 */
public class RoundProgressView extends View {
    private static final String TAG = "RoundProgressView";
    private Paint mBgCirclePaint, mPercentCirclePaint, mTestPaint;
    private int mBgCircleColor = Color.parseColor("#EAF3F9");//普通的颜色
    private int mPercentCircleColor = Color.parseColor("#BADAFF");//已经走了的进度条颜色
    private int viewWidth = DEFAULT_WIDTH;
    private int viewHeight = DEFAULT_HEIGHT;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int CIRCLE_PADDING = 0;
    private static final int CIRCLE_STROKE = 40;//线条宽度
    private static final float PERCENT_CIRCLE_START_ANGLE = 90;//起始角度
    private static final Paint.Style progress_style = Paint.Style.STROKE;//填充式还是环形式
    private float textSize = 20;//文字大小
    private int percent = 90;//进度条
    private RectF rectF = new RectF();

    public RoundProgressView(Context context) {
        this(context, null);
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);
        textSize = array.getDimension(R.styleable.RoundProgressView_textsize, textSize);
//        strokeWidth = array.getInteger(R.styleable.RoundProgressView_stroke_width, CIRCLE_STROKE);
        mBgCircleColor = array.getColor(R.styleable.RoundProgressView_normal_color, mBgCircleColor);
//        mPercentCircleColor = array.getColor(R.styleable.RoundProgressView_mPercentCircleColor, mPercentCircleColor);
        percent = array.getInt(R.styleable.RoundProgressView_progress, percent);
        array.recycle();
        initPaint();
    }

    // 2.初始化画笔
    private void initPaint() {
        mBgCirclePaint = new Paint();
        mBgCirclePaint.setColor(mBgCircleColor);       //设置画笔颜色
        mBgCirclePaint.setAntiAlias(true);
        mBgCirclePaint.setStyle(progress_style);  //设置画笔模式为描边
        mBgCirclePaint.setStrokeWidth(CIRCLE_STROKE);         //设置画笔宽度为10px
        mBgCirclePaint.setAntiAlias(true); // 抗锯齿
        mPercentCirclePaint = new Paint();

        mPercentCirclePaint.setColor(mPercentCircleColor);       //设置画笔颜色
        mPercentCirclePaint.setAntiAlias(true);
        mPercentCirclePaint.setStyle(progress_style);  //设置画笔模式为描边
        mPercentCirclePaint.setStrokeWidth(CIRCLE_STROKE);         //设置画笔宽度为10px
        mPercentCirclePaint.setAntiAlias(true); // 抗锯齿

        mTestPaint = new Paint();
        mTestPaint.setColor(Color.RED);
        mPercentCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            // match_parent & DP
            viewWidth = widthSize;
            viewHeight = heightSize;
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            // warp_content
            viewWidth = DEFAULT_WIDTH;
            viewHeight = DEFAULT_HEIGHT;
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int padding = CIRCLE_PADDING + CIRCLE_STROKE;
        float radiusW = (float) viewWidth / 2;
        float radiusH = (float) viewHeight / 2;
        float radius = radiusW - padding;
        canvas.drawCircle(radiusW, radiusH, radius, mBgCirclePaint);
        if (percent > 0) {
            initRctfF(padding);
            float STROKE_RADIUS = (float) CIRCLE_STROKE / 2;
            float deviationAngle = clcaTan(STROKE_RADIUS, radiusH - CIRCLE_PADDING - STROKE_RADIUS);
            float v = clacStartArcLocation(radius);
            float v1 = clacStartArcY(radius);
            canvas.drawArc(rectF, PERCENT_CIRCLE_START_ANGLE + deviationAngle, percent - deviationAngle, progress_style == Paint.Style.FILL, mPercentCirclePaint);
            canvas.drawCircle(radiusW - STROKE_RADIUS + v, viewHeight - (CIRCLE_PADDING + CIRCLE_STROKE), STROKE_RADIUS, mTestPaint);
//            canvas.drawCircle(radiusW - STROKE_RADIUS + v, viewHeight - (CIRCLE_PADDING + CIRCLE_STROKE), STROKE_RADIUS, mTestPaint);
        }
        canvas.drawLine(viewWidth / 2, 0, viewWidth / 2, viewHeight, mTestPaint);
    }


    /**
     * 计算小弧形X轴偏移量
     *
     * @param radius
     * @return
     */
    public float clacStartArcLocation(float radius) {
        float v = (radius + (float) CIRCLE_STROKE / 2) / CIRCLE_STROKE;
        Log.e(TAG, "clacStartArcLocation " + CIRCLE_STROKE / v);
        return CIRCLE_STROKE / v / 2;
    }

    public float clacStartArcY(float radius) {
        float v = (radius + (float) CIRCLE_STROKE / 2) / CIRCLE_STROKE;
        Log.e(TAG, "clacStartArcLocation " + (radius - (float) CIRCLE_STROKE / 2) / v);
        return (radius - (float) CIRCLE_STROKE / 2) / v / 2;
    }

    private void initRctfF(int padding) {
        rectF.left = padding;
        rectF.top = padding;
        rectF.right = viewWidth - padding;
        rectF.bottom = viewHeight - padding;
    }


    /**
     * 直接设置百分比 max 360 min  0
     *
     * @param percent
     */

    public void setPercent(@IntRange(from = 0, to = 360) int percent) {
        this.percent = percent;
    }

    /**
     * 根据分数计算
     *
     * @param number
     */
    public void setNumerical(@IntRange(from = 0, to = 100) int number) {
        setPercent(number * 360 / 100);
    }

    /**
     * 计算圆弧偏移角度
     *
     * @param x
     * @param y
     * @return
     */
    public float clcaTan(double x, double y) {
        if (x < 0 || y < 0) {
            return 0;
        }
        double tan = Math.atan2(x, y);
        Log.e(TAG, "tan " + tan);
        Log.e(TAG, "tan " + 180 * tan / Math.PI);
        return (float) (180 * tan / Math.PI);
    }


}
