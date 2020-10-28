package com.view.ViewDragHelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;


/**
 * Created by GuoXu on 2020/8/17 18:42.
 */
public class BottomDrawerLayout extends ViewGroup {
    public static final String TAG = "BottomDrawerLayout";
    private final ViewDragHelper viewDragHelper;
    private View bgView;
    private View bottomView;
    private int t1;
    private ViewGroup childAt;
    private ExpansionStatus expansionStatus = ExpansionStatus.NO_Expansion;

    public enum ExpansionStatus {
        NO_Expansion,
        FIRST_Expansion,
        SECOD_Expansion
    }


    public BottomDrawerLayout(Context context) {
        this(context, null);
    }

    public BottomDrawerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BottomDrawerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new BottomDragHelperCallBack(this));
        //设置边缘手势生效
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        int maxWidth = 0;
        int mLeftHeight = 0;
        int mLeftWidth = 0;

        final int count = getChildCount();
        Log.d(TAG, "Child count is " + count);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        Log.d(TAG, "widthSize in Measure is :" + widthSize);

        // 遍历我们的子View，并测量它们，根据它们要求的尺寸进而计算我们的StaggerLayout需要的尺寸。
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            //可见性为gone的子View，我们就当它不存在。
            if (child.getVisibility() == GONE)
                continue;

            // 测量该子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //简单地把所有子View的测量宽度相加。
            maxWidth += child.getMeasuredWidth();
            mLeftWidth += child.getMeasuredWidth();

            //这里判断是否需将index 为i的子View放入下一行，如果需要，就要更新我们的maxHeight，mLeftHeight和mLeftWidth。
            if (mLeftWidth > widthSize) {
                maxHeight += mLeftHeight;
                mLeftWidth = child.getMeasuredWidth();
                mLeftHeight = child.getMeasuredHeight();

            } else {

                mLeftHeight = Math.max(mLeftHeight, child.getMeasuredHeight());
            }

        }

        //这里把最后一行的高度加上，注意不要遗漏。
        maxHeight += mLeftHeight;

        //这里将宽度和高度与Google为我们设定的建议最低宽高对比，确保我们要求的尺寸不低于建议的最低宽高。
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        //报告我们最终计算出的宽高。
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final View childBg = getChildAt(0);
        childBg.layout(0, 0, getWidth(), getHeight());
        childAt = (ViewGroup) getChildAt(1);
        t1 = getHeight() - childAt.getChildAt(0).getMeasuredHeight();
        childAt.layout(0, t1, getWidth(), childAt.getMeasuredHeight() + t1);
        childAt.getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float curTranslationX = childAt.getTranslationY();
                switch (expansionStatus) {
                    case NO_Expansion:
                        Log.e(TAG, "NO_Expansion" + -childAt.getChildAt(1).getMeasuredHeight());
                        Log.e(TAG, "NO_Expansion" + curTranslationX);
                        ObjectAnimator moveIn = ObjectAnimator.ofFloat(childAt, "translationY",
                                curTranslationX, -childAt.getChildAt(1).getMeasuredHeight());
                        moveIn.setDuration(500);
                        moveIn.start();
                        expansionStatus = ExpansionStatus.FIRST_Expansion;
                        break;
                    case FIRST_Expansion:
                        Log.e(TAG, "FIRST_Expansion A " + childAt.getMeasuredHeight());
                        Log.e(TAG, "FIRST_Expansion B " + childAt.getChildAt(0).getMeasuredHeight());
                        Log.e(TAG, "FIRST_Expansion C " + curTranslationX);
                        ObjectAnimator moveIn1 = ObjectAnimator.ofFloat(childAt, "translationY",
                                curTranslationX, -childAt.getMeasuredHeight() + childAt.getChildAt(0).getMeasuredHeight());
                        moveIn1.setDuration(500);
                        moveIn1.start();
                        expansionStatus = ExpansionStatus.SECOD_Expansion;
                        break;
                    case SECOD_Expansion:
                        int i = childAt.getChildAt(1).getMeasuredHeight() + childAt.getChildAt(2).getMeasuredHeight();
                        Log.e(TAG, "SECOD_Expansion" + i);
                        Log.e(TAG, "SECOD_Expansion" + curTranslationX);
                        ObjectAnimator moveIn2 = ObjectAnimator.ofFloat(childAt, "translationY", curTranslationX, 0);
                        moveIn2.setDuration(500);
                        moveIn2.start();
                        expansionStatus = ExpansionStatus.NO_Expansion;
                        break;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bgView = getChildAt(0);
        bottomView = getChildAt(1);
    }

    /**
     * 拖拽回调
     */
    public class BottomDragHelperCallBack extends ViewDragHelper.Callback {
        private BottomDrawerLayout parentRootView;

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return false;
        }

        public BottomDragHelperCallBack(BottomDrawerLayout bottomDrawerLayout) {
            parentRootView = bottomDrawerLayout;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (changedView == bottomView)
                Log.e(TAG, "onViewPositionChanged  left " + left + "top " + top + "dx " + dx + "dy " + dy);
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == bottomView)
                Log.e(TAG, "onViewReleased  xvel " + xvel + "yvel " + yvel);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            viewDragHelper.captureChildView(bottomView, pointerId);
        }

        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int paddingTop = parentRootView.getPaddingTop();
            int paddingbottom = parentRootView.getHeight() - bottomView.getHeight() - paddingTop;
            final int newLeft = Math.min(Math.max(top, getHeight() - childAt.getHeight()), t1);
            return newLeft;
        }
    }
}
