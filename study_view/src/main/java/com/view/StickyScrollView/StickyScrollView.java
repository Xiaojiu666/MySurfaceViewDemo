package com.view.StickyScrollView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

import androidx.core.widget.NestedScrollView;


import com.sn.study_pic.R;

import java.util.ArrayList;

public class StickyScrollView extends HorizontalScrollView {

    /**
     * Tag for views that should stick and have constant drawing. e.g. TextViews, ImageViews etc
     */
    public static final String STICKY_TAG = "sticky";

    /**
     * Flag for views that should stick and have non-constant drawing. e.g. Buttons, ProgressBars etc
     */
    public static final String FLAG_NONCONSTANT = "-nonconstant";

    /**
     * Flag for views that have aren't fully opaque
     * 可以用来控制是否隐藏添加了标签的原控件
     */
    public static final String FLAG_HASTRANSPARANCY = "-hastransparancy";
    /**
     * Default height of the shadow peeking out below the stuck view.
     */
    private static final int DEFAULT_SHADOW_HEIGHT = 10; // dp;
    private static final String TAG = "StickyScrollView";

    public boolean model = false;

    private ArrayList<View> stickyViews;
    private View currentlyStickingView;
    private float stickyViewTopOffset;
    private int stickyViewLeftOffset;
    private boolean redirectTouchesToStickyView;
    private boolean clippingToPadding;
    private boolean clipToPaddingHasBeenSet;

    private int mShadowHeight;
    private Drawable mShadowDrawable;

    private final Runnable invalidateRunnable = new Runnable() {

        @Override
        public void run() {
            if (currentlyStickingView != null) {
                int l = getLeftForViewRelativeOnlyChild(currentlyStickingView);
                int t = getBottomForViewRelativeOnlyChild(currentlyStickingView);
                int r = getRightForViewRelativeOnlyChild(currentlyStickingView);
                int b = (int) (getScrollY() + (currentlyStickingView.getHeight() + stickyViewTopOffset));
                invalidate(l, t, r, b);
            }
            postDelayed(this, 16);
        }
    };

    public StickyScrollView(Context context) {
        this(context, null);
    }

    public StickyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.scrollViewStyle);
    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.StickyScrollView, defStyle, 0);

        final float density = context.getResources().getDisplayMetrics().density;
        int defaultShadowHeightInPix = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);

        mShadowHeight = a.getDimensionPixelSize(
                R.styleable.StickyScrollView_stuckShadowHeight,
                defaultShadowHeightInPix);

        int shadowDrawableRes = a.getResourceId(
                R.styleable.StickyScrollView_stuckShadowDrawable, -1);

        if (shadowDrawableRes != -1) {
            mShadowDrawable = context.getResources().getDrawable(
                    shadowDrawableRes);
        }

        a.recycle();
    }

    /**
     * Sets the height of the shadow drawable in pixels.
     *
     * @param height
     */
    public void setShadowHeight(int height) {
        mShadowHeight = height;
    }


    public void setup() {
        stickyViews = new ArrayList<View>();
    }

    private int getLeftForViewRelativeOnlyChild(View v) {
        int left = v.getLeft();
        if (v != null) {
            while (v.getParent() != getChildAt(0)) {
                v = (View) v.getParent();
                if (v != null) {
                    left += v.getLeft();
                }
            }
        }
        return left;
    }

    private int getTopForViewRelativeOnlyChild(View v) {
        int top = v.getTop();
        if (v != null) {
            while (v.getParent() != getChildAt(0)) {
                v = (View) v.getParent();
                if (v != null) {
                    top += v.getTop();
                }
            }
        }
        return top;
    }

    private int getRightForViewRelativeOnlyChild(View v) {
        int right = v.getRight();
        if (v != null) {
            while (v.getParent() != getChildAt(0)) {
                v = (View) v.getParent();
                if (v != null) {
                    right += v.getRight();
                }
            }
        }
        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v) {
        int bottom = v.getBottom();
        if (v != null) {
            while (v.getParent() != getChildAt(0)) {
                v = (View) v.getParent();
                if (v != null) {
                    bottom += v.getBottom();
                }
            }
        }
        return bottom;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!clipToPaddingHasBeenSet) {
            clippingToPadding = true;
        }
        notifyHierarchyChanged();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        Log.e(TAG, "setClipToPadding" + clipToPadding);
        clippingToPadding = clipToPadding;
        clipToPaddingHasBeenSet = true;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        findStickyViews(child);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (currentlyStickingView != null) {
            canvas.save();
            if (false) {
                canvas.translate(getPaddingLeft() + stickyViewLeftOffset, getScrollY() + stickyViewTopOffset + (clippingToPadding ? getPaddingTop() : 0));
                canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0),
                        getWidth() - stickyViewLeftOffset,
                        currentlyStickingView.getHeight() + mShadowHeight + 1);
                if (mShadowDrawable != null) {
                    int left = 0;
                    int right = currentlyStickingView.getWidth();
                    int top = currentlyStickingView.getHeight();
                    int bottom = currentlyStickingView.getHeight() + mShadowHeight;
                    mShadowDrawable.setBounds(left, top, right, bottom);
                    mShadowDrawable.draw(canvas);
                }
                canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(), currentlyStickingView.getHeight());
//            if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
                showView(currentlyStickingView);
                currentlyStickingView.draw(canvas);
                hideView(currentlyStickingView);
//            } else {
//                currentlyStickingView.draw(canvas);
//            }
            } else {
                Log.e(TAG, "stickyViewLeftOffset" + stickyViewLeftOffset);
                canvas.translate(getScrollX() + stickyViewTopOffset + (clippingToPadding ? getPaddingLeft() : 0), getPaddingTop() + stickyViewLeftOffset);
                canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0),
                        getHeight() - stickyViewLeftOffset,
                        currentlyStickingView.getWidth() + mShadowHeight + 1);
                if (mShadowDrawable != null) {
                    int left = 0;
                    int right = currentlyStickingView.getWidth();
                    int top = currentlyStickingView.getHeight();
                    int bottom = currentlyStickingView.getHeight() + mShadowHeight;
                    mShadowDrawable.setBounds(left, top, right, bottom);
                    mShadowDrawable.draw(canvas);
                }
                canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(), currentlyStickingView.getHeight());
//            if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
                showView(currentlyStickingView);
                currentlyStickingView.draw(canvas);
                hideView(currentlyStickingView);
            }

            canvas.restore();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            redirectTouchesToStickyView = true;
        }

        if (redirectTouchesToStickyView) {
            redirectTouchesToStickyView = currentlyStickingView != null;
            if (redirectTouchesToStickyView) {
                redirectTouchesToStickyView =
                        ev.getY() <= (currentlyStickingView.getHeight() + stickyViewTopOffset) &&
                                ev.getX() >= getLeftForViewRelativeOnlyChild(currentlyStickingView) &&
                                ev.getX() <= getRightForViewRelativeOnlyChild(currentlyStickingView);
            }
        } else if (currentlyStickingView == null) {
            redirectTouchesToStickyView = false;
        }
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(0, -1 * ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean hasNotDoneActionDown = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(0, ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            hasNotDoneActionDown = false;
        }

        if (hasNotDoneActionDown) {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            super.onTouchEvent(down);
            hasNotDoneActionDown = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            hasNotDoneActionDown = true;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        doTheStickyThing();
    }

    private void doTheStickyThing() {
        View viewThatShouldStick = null;
        View approachingView = null;
        for (View v : stickyViews) {
            int viewleft = getLeftForViewRelativeOnlyChild(v) - getScrollX() - getPaddingLeft();
            Log.e(TAG, "viewleft " + viewleft);
            if (viewleft < 0) {
                if (viewThatShouldStick == null || viewleft > (getLeftForViewRelativeOnlyChild(viewThatShouldStick) - getScrollX() + (clippingToPadding ? 0 : getPaddingLeft()))) {
                    viewThatShouldStick = v;
                }
            } else {
                if (approachingView == null || viewleft < (getLeftForViewRelativeOnlyChild(approachingView) - getScrollX() + (clippingToPadding ? 0 : getPaddingLeft()))) {
                    approachingView = v;
                }
            }
        }

        if (viewThatShouldStick != null) {
            Log.e(TAG, "viewThatShouldStick true");
            stickyViewTopOffset = approachingView == null ? 0 : Math.min(0, getLeftForViewRelativeOnlyChild(approachingView) - getScrollX() + (clippingToPadding ? 0 : getPaddingLeft()) - viewThatShouldStick.getWidth());
            if (viewThatShouldStick != currentlyStickingView) {
                Log.e(TAG, "viewThatShouldStick viewThatShouldStick != currentlyStickingView");
                if (currentlyStickingView != null) {
                    stopStickingCurrentlyStickingView();
                }
                // only compute the left offset when we start sticking.
                stickyViewLeftOffset = getTopForViewRelativeOnlyChild(viewThatShouldStick);
                startStickingView(viewThatShouldStick);
            }
        } else if (currentlyStickingView != null) {
            Log.e(TAG, "currentlyStickingView true");
            stopStickingCurrentlyStickingView();
        }
    }
//    private void doTheStickyThing() {
//        View viewThatShouldStick = null; //记录last view
//        View approachingView = null; // 记录当前在顶部的 view
//        for (View v : stickyViews) {
//            int viewTop = getTopForViewRelativeOnlyChild(v) - getScrollY() - getPaddingTop();
////            Log.e(TAG, "viewTop " + viewTop);
//            if (viewTop <= 0) {
////                Log.e(TAG, "viewTop <= 0");
//                if (viewThatShouldStick == null || viewTop > (getTopForViewRelativeOnlyChild(viewThatShouldStick) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop()))) {
////                    Log.e(TAG, " viewThatShouldStick = v");
//                    viewThatShouldStick = v;
//                }
//            } else {
//                if (approachingView == null || viewTop < (getTopForViewRelativeOnlyChild(approachingView) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop()))) {
////                    Log.e(TAG, " approachingView = v");
//                    approachingView = v;
//                }
//            }
//        }
//        if (viewThatShouldStick != null) {
//            // approachingView 全部都显示出来的时候，approachingView == null
//            stickyViewTopOffset = approachingView == null ? 0 : Math.min(0, getTopForViewRelativeOnlyChild(approachingView) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop()) - viewThatShouldStick.getHeight());
//            Log.e(TAG, "stickyViewTopOffset " + stickyViewTopOffset);
//            Log.e(TAG, "getTopForViewRelativeOnlyChild(approachingView) " + getTopForViewRelativeOnlyChild(approachingView));
//            Log.e(TAG, "getScrollY() " + getScrollY());
////            Log.e(TAG, "viewThatShouldStick.getHeight() " + viewThatShouldStick.getHeight());73
//            if (viewThatShouldStick != currentlyStickingView) {
//                Log.e(TAG, "viewThatShouldStick != currentlyStickingView");
//                if (currentlyStickingView != null) {
//                    Log.e(TAG, "currentlyStickingViewA != null");
//                    stopStickingCurrentlyStickingView();
//                }
//                // only compute the left offset when we start sticking.
//                stickyViewLeftOffset = getLeftForViewRelativeOnlyChild(viewThatShouldStick);
//                startStickingView(viewThatShouldStick);
//            }
//        } else if (currentlyStickingView != null) {
//            Log.e(TAG, "currentlyStickingViewB != null");
//            stopStickingCurrentlyStickingView();
//        }
//    }

    private void startStickingView(View viewThatShouldStick) {
        currentlyStickingView = viewThatShouldStick;
//        if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
        hideView(currentlyStickingView);
//        }
        if (((String) currentlyStickingView.getTag()).contains(FLAG_NONCONSTANT)) {
            post(invalidateRunnable);
        }
    }

    private void stopStickingCurrentlyStickingView() {
//        if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
        showView(currentlyStickingView);
//        }
        currentlyStickingView = null;
        removeCallbacks(invalidateRunnable);
    }

    /**
     * Notify that the sticky attribute has been added or removed from one or more views in the View hierarchy
     */
    public void notifyStickyAttributeChanged() {
        notifyHierarchyChanged();
    }

    private void notifyHierarchyChanged() {
        if (currentlyStickingView != null) {
            stopStickingCurrentlyStickingView();
        }
        stickyViews.clear();
        findStickyViews(getChildAt(0));
        doTheStickyThing();
        invalidate();
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return -paddingTop;
    }

    private int paddingTop = 0;

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    private void findStickyViews(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                String tag = getStringTagForView(vg.getChildAt(i));
                if (tag != null && tag.contains(STICKY_TAG)) {
                    stickyViews.add(vg.getChildAt(i));
                } else if (vg.getChildAt(i) instanceof ViewGroup) {
                    findStickyViews(vg.getChildAt(i));
                }
            }
        } else {
            String tag = (String) v.getTag();
            if (tag != null && tag.contains(STICKY_TAG)) {
                stickyViews.add(v);
            }
        }
    }

    private String getStringTagForView(View v) {
        Object tagObject = v.getTag();
        return String.valueOf(tagObject);
    }

    private void hideView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setAlpha(0);
        } else {
            AlphaAnimation anim = new AlphaAnimation(1, 0);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }

    private void showView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setAlpha(1);
        } else {
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }

}