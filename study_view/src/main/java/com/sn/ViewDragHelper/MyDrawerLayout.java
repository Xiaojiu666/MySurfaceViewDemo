package com.sn.ViewDragHelper;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by GuoXu on 2020/8/17 15:55.
 * 参考资料 :https://blog.csdn.net/lmj623565791/article/details/46858663
 */
public class MyDrawerLayout extends LinearLayout {

    public static final String TAG = "MyDrawerLayout";
    private ViewDragHelper viewDragHelper;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    public MyDrawerLayout(Context context) {
        this(context, null);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new MyViewDragHelperCallBack(this));
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置边缘手势生效
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private Point mAutoBackOriginPos = new Point();


    public class MyViewDragHelperCallBack extends ViewDragHelper.Callback {

        private MyDrawerLayout myDrawerLayout;

        public MyViewDragHelperCallBack(MyDrawerLayout myDrawerLayout) {
            this.myDrawerLayout = myDrawerLayout;
        }

        /**
         * 告诉ViewDragHelper，哪个View 可以滑动
         *
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragView || child == mAutoBackView;
        }

        /**
         * 告诉ViewDragHelper，哪个View 横向最大/最小可以滑动距离
         *
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e(TAG, "left " + left + "dx " + dx);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mDragView.getWidth() - leftBound; // 780
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            // retrun 得值为 子View 左侧 最终能到达的值，不做限制 由left决定
            return child == mDragView ? newLeft : left;
        }

        /**
         * 告诉ViewDragHelper，哪个View 纵向最大/最小可以滑动距离
         *
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int paddingTop = getPaddingTop();
            int paddingbottom = getHeight() - mDragView.getHeight() - paddingTop;
            Log.e(TAG, "paddingTop" + paddingTop + "paddingbottom" + paddingbottom + "top" + top);
            final int newLeft = Math.min(Math.max(top, paddingTop), paddingbottom);
            return newLeft;
        }

        /**
         * 某个View手指释放的时候回调
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //mAutoBackView手指释放时可以自动回去
            if (releasedChild == mAutoBackView) {
                //该方法与smoothSlideViewTo所实现的效果是一样的，区别在于smoothSlideViewTo（）是指定一个子View，
                // 而settleCapturedViewAt（）是在ViewDragHelper.Callback的onViewReleased（）方法中获取当前释放的子View，然后实现手势惯性的效果。
                viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                invalidate();
            }
        }

        /**
         * 在边界移动式
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //通过captureChildView对其进行捕获，该方法可以绕过tryCaptureView
            viewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
        }

        /**
         * 解决ziView 设置点击事件和当前滑动冲突
         *
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    /**
     * 完成绘制
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }


    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
