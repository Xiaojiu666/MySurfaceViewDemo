package com.view.ExPansionView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


/**
 * Created by GuoXu on 2020/8/17 18:42.
 */
public class ExpansionLayout extends ViewGroup implements View.OnClickListener {
    public static final String TAG = "ExpansionLayout";
    private int expansionViewTop;
    private ViewGroup expansionView;
    private View expansionBgView;
    private ExpansionStatus expansionStatus = ExpansionStatus.NO_Expansion;
    private int maxExpansionPosi;


    @Override
    public void onClick(View v) {
        if (v == expansionView.getChildAt(0)) {
            float curTranslationX = expansionView.getTranslationY();
            switch (expansionStatus) {
                case NO_Expansion:
                    ViewGroup scorllView = (ViewGroup) expansionView.getChildAt(1);
                    ViewGroup linerlayout = (ViewGroup) scorllView.getChildAt(0);
                    int measuredHeight = linerlayout.getChildAt(0).getMeasuredHeight();
                    tarnAnmi(curTranslationX, -measuredHeight, ExpansionStatus.FIRST_Expansion);
                    break;
                case FIRST_Expansion:
                    /**
                     * 控制向上展开最高高度，
                     * 整体展开的View不能超过ViewGroup的4/5
                     */
                    int ExTopViewHeight = expansionView.getChildAt(0).getMeasuredHeight();
                    int min = Math.min(expansionView.getMeasuredHeight() -
                            ExTopViewHeight, maxExpansionPosi - ExTopViewHeight);
                    if (min == (maxExpansionPosi - ExTopViewHeight)) {
                        expansionView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, maxExpansionPosi));
                    }
                    tarnAnmi(curTranslationX, -min
                            , ExpansionStatus.SECOD_Expansion);
                    break;
                case SECOD_Expansion:
                    tarnAnmi(curTranslationX, 0, ExpansionStatus.NO_Expansion);
                    break;
            }
        }
    }

    private void tarnAnmi(float curTranslationX, int i, ExpansionStatus first_expansion) {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(expansionView, "translationY", curTranslationX, i);
        moveIn.setDuration(500);
        moveIn.start();
        expansionStatus = first_expansion;
    }

    public enum ExpansionStatus {
        NO_Expansion,
        FIRST_Expansion,
        SECOD_Expansion
    }


    public ExpansionLayout(Context context) {
        this(context, null);
    }

    public ExpansionLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ExpansionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
        maxExpansionPosi = getHeight() / 5 * 4;
        //摆放BgView的宽高，铺满this view
        expansionBgView.layout(0, 0, getWidth(), getHeight());
        expansionViewTop = getHeight() - expansionView.getChildAt(0).getMeasuredHeight();
        expansionView.layout(0, expansionViewTop, getWidth(), expansionView.getMeasuredHeight() + expansionViewTop);
        // 摆放折叠View的位置
        if (ExpansionStatus.NO_Expansion == expansionStatus) {

        }
//        else if (ExpansionStatus.FIRST_Expansion == expansionStatus) {
//            expansionViewTop = getHeight() - expansionView.getChildAt(0).getMeasuredHeight();
//            ViewGroup scorllView = (ViewGroup) expansionView.getChildAt(1);
//            ViewGroup linerlayout = (ViewGroup) scorllView.getChildAt(0);
//            int expansionViewTop1 = expansionViewTop - linerlayout.getChildAt(0).getMeasuredHeight();
//            expansionView.layout(0, expansionViewTop1, getWidth(), expansionView.getMeasuredHeight() + expansionViewTop);
//        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        expansionBgView = getChildAt(0);
        expansionView = (ViewGroup) getChildAt(1);
        expansionView.getChildAt(0).setOnClickListener(this);
    }

}
