package com.sn.ViewDragHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by GuoXu on 2020/10/19 17:22.
 */
public class SlidingExpansionView extends FrameLayout {

    public static final String TAG = "SlidingExpansionView";
    private ViewDragHelper viewDragHelper;
    private View mBgView;
    private View mChildSlidingView;

    public SlidingExpansionView(Context context) {
        this(context, null);
    }

    public SlidingExpansionView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SlidingExpansionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new SlidingViewDragHelper(this));
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

    public class SlidingViewDragHelper extends ViewDragHelper.Callback {

        private SlidingExpansionView slidingExpansionView;

        public SlidingViewDragHelper(SlidingExpansionView slidingExpansionView) {
            this.slidingExpansionView = slidingExpansionView;
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
            return child == mChildSlidingView;
        }

        /**
         * 告诉ViewDragHelper，哪个View 横向最大/最小可以滑动距离
         *
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        /**
         * 告诉ViewDragHelper，哪个View 纵向最大/最小可以滑动距离
         *
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int i = getHeight() - child.getHeight();
            int q1 = mBgView.getMeasuredHeight();
            Log.e(TAG, " i " + i + "q1 " + q1 + "top " + top);
            final int newLeft = Math.min(Math.max(top, q1 / 2), q1);
            return newLeft;
        }

        /**
         * 某个View手指释放的时候回调
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //mAutoBackView手指释放时可以自动回去
            if (releasedChild == mChildSlidingView) {
                int top = mChildSlidingView.getTop();
                Log.e(TAG, "top : " + top + "yvel : " + yvel);
                int centerX = mBgView.getMeasuredHeight() * 3 / 4;
                int moveTop;
                if (top >= centerX) {
                    moveTop = mBgView.getMeasuredHeight();
                } else {
                    moveTop = mBgView.getMeasuredHeight() / 2;
                }
                viewDragHelper.settleCapturedViewAt(0, moveTop);
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
            viewDragHelper.captureChildView(mChildSlidingView, pointerId);
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

    /**
     * 完成绘制
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBgView = getChildAt(0);
        mChildSlidingView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutVertical(l, t, r, b);
    }

    private void layoutVertical(int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child == null) {
//                childTop += measureNullChild(i);
            } else if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                Log.e(TAG, "childWidth : " + childWidth + "childHeight ： " + childHeight);
                final int layoutDirection = getLayoutDirection();
                if (i == 0) {
                    setChildFrame(child, 0, 0,
                            childWidth, childHeight);
                } else if (i == 1) {
                    int startTop = mBgView.getMeasuredHeight() / 2;
                    setChildFrame(child, 0, startTop,
                            childWidth, childHeight);
                }
            }
        }
    }

    public void clacScrollPoint() {

    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    // 回弹效果需要重写
    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
