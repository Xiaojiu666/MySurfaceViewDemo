package com.view.ViewDragHelper;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;


import com.NoDoubleClickUtils;
import com.sn.study_pic.R;

import java.util.List;

/**
 * @作者 luckly
 * @创建日期 2019/7/8 11:43
 */
public class CaterpillarIndicator extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "CaterpillarIndicator";

    private static final int BASE_ID = 0xffff00;
    private static final int FOOTER_COLOR = 0xFFFFC445;
    private static final int ITEM_TEXT_COLOR_NORMAL = 0xFF999999;
    private static final int ITEM_TEXT_COLOR_SELECT = 0xFFFFC445;
    private static final int TEXT_CENTER = 0;
    private static final int LINE_CENTER = 1;

    private boolean isRoundRectangleLine = true;
    private boolean isCaterpillar = true;
    private int mFootLineColor;
    private int mTextSizeNormal;
    private int mTextSizeSelected;
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mFooterLineHeight;
    private int mItemLineWidth;
    /**
     * item count
     */
    private int mItemCount = 0;
    private int textCenterFlag;
    private int mCurrentScroll = 0;
    private int mSelectedTab = 0;
    private int linePaddingBottom = 0;
    private boolean isNewClick;
    private List<TitleInfo> mTitles;
    private ViewPager mViewPager;
    /**
     * indicator line is Rounded Rectangle
     */

    /**
     * line paint
     */
    private Paint mPaintFooterLine;
    /**
     * line RectF
     */
    private RectF drawLineRect;
    private int lastPosition;
    private boolean mHasPic;//是否有图片
    private int textWidth;

    public interface onItemDoubleClickListener {
        void setOnItemDoubleClickListener(int position);
    }

    private onItemDoubleClickListener onItemDoubleClickListener;


    public CaterpillarIndicator(Context context) {
        this(context, null);
    }

    public CaterpillarIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CaterpillarIndicator);
        mFootLineColor = a.getColor(R.styleable.CaterpillarIndicator_slide_footer_color, FOOTER_COLOR);
        mTextSizeNormal = a.getDimensionPixelSize(R.styleable.CaterpillarIndicator_slide_text_size_normal, dip2px(mTextSizeNormal));
        mTextSizeSelected = a.getDimensionPixelSize(R.styleable.CaterpillarIndicator_slide_text_size_selected, dip2px(mTextSizeNormal));
        mFooterLineHeight = a.getDimensionPixelOffset(R.styleable.CaterpillarIndicator_slide_footer_line_height, dip2px(3));
        mTextColorSelected = a.getColor(R.styleable.CaterpillarIndicator_slide_text_color_selected, ITEM_TEXT_COLOR_SELECT);
        mTextColorNormal = a.getColor(R.styleable.CaterpillarIndicator_slide_text_color_normal, ITEM_TEXT_COLOR_NORMAL);
        isCaterpillar = a.getBoolean(R.styleable.CaterpillarIndicator_slide_caterpillar, true);
        isRoundRectangleLine = a.getBoolean(R.styleable.CaterpillarIndicator_slide_round, true);
        mItemLineWidth = (int) a.getDimension(R.styleable.CaterpillarIndicator_slide_item_width, dip2px(30));
        linePaddingBottom = (int) a.getDimension(R.styleable.CaterpillarIndicator_slide_padding_bottom, 0);
        textCenterFlag = a.getInt(R.styleable.CaterpillarIndicator_slide_text_center_flag, TEXT_CENTER);

        setWillNotDraw(false);
        initDraw();
        a.recycle();
    }


    /**
     * 底部线高
     *
     * @param mFooterLineHeight
     */
    public void setFooterLineHeight(int mFooterLineHeight) {
        this.mFooterLineHeight = dip2px(mFooterLineHeight);
        invalidate();
    }

    public void setLinePaddingBottom(int paddingBottom) {
        this.linePaddingBottom = paddingBottom;
        invalidate();
    }

    public void setTextCenterFlag(int centerFlag) {
        this.textCenterFlag = centerFlag;
        invalidate();
    }


    /**
     * item width
     *
     * @param mItemLineWidth item width(dp)
     */
    public void setItemLineWidth(int mItemLineWidth) {
        this.mItemLineWidth = dip2px(mItemLineWidth);
        invalidate();
    }


    public void setCaterpillar(boolean caterpillar) {
        isCaterpillar = caterpillar;
        invalidate();
    }

    /**
     * is round line
     *
     * @param roundRectangleLine true (yes) false (no )
     */
    public void setRoundRectangleLine(boolean roundRectangleLine) {
        isRoundRectangleLine = roundRectangleLine;
    }

    private void initDraw() {
        mPaintFooterLine = new Paint();
        mPaintFooterLine.setAntiAlias(true);
        mPaintFooterLine.setStyle(Paint.Style.FILL);
        drawLineRect = new RectF(0, 0, 0, 0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float a = (float) getWidth() / (float) mViewPager.getWidth();
        onScrolled((int) ((getWidth() + mViewPager.getPageMargin()) * position + positionOffsetPixels * a));
    }

    @Override
    public void onPageSelected(int position) {
        onSwitched(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setFootLineColor(int mFootLineColor) {
        this.mFootLineColor = mFootLineColor;
        invalidate();
    }


    /**
     * set text normal size(dp)
     *
     * @param mTextSizeNormal normal text size
     */
    public void setTextSizeNormal(int mTextSizeNormal) {
        this.mTextSizeNormal = dip2px(mTextSizeNormal);
        updateItemText();
    }

    public void setTextSizeSelected(int mTextSizeSelected) {
        this.mTextSizeSelected = dip2px(mTextSizeSelected);
        updateItemText();
    }

    public void setTextColorNormal(int mTextColorNormal) {
        this.mTextColorNormal = mTextColorNormal;
        updateItemText();
    }

    public void setTextColorSelected(int mTextColorSelected) {
        this.mTextColorSelected = mTextColorSelected;
        updateItemText();
    }

    private void updateItemText() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                if (tv.isSelected()) {
                    tv.setTextColor(mTextColorSelected);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeSelected);
                } else {
                    tv.setTextColor(mTextColorNormal);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal);
                }
            }
        }

    }


    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 底部指示器线 是否包裹文字
    public boolean isIndicatorWarpText = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isNewClick) {
            isNewClick = false;
            return;
        }
        float scroll_x;
        int cursorWidth;
        if (mItemCount != 0) {
            cursorWidth = getChildAt(mSelectedTab).getWidth();
            scroll_x = (mCurrentScroll - ((mSelectedTab) * (getWidth()))) / mItemCount;
        } else {
            cursorWidth = getWidth();
            scroll_x = mCurrentScroll;
        }

        this.mItemLineWidth = cursorWidth - (cursorWidth - mItemLineWidth) / 2;
        int mItemLeft;
        int mItemRight;
        if (mItemLineWidth < cursorWidth) {
            mItemLeft = (cursorWidth - mItemLineWidth) / 2;
            mItemRight = cursorWidth - mItemLeft;
        } else {
            mItemLeft = 0;
            mItemRight = cursorWidth;
        }
        float leftX = 0.0f;
        float rightX = 0.0f;
        mPaintFooterLine.setColor(mFootLineColor);
        if (isCaterpillar) {
            RelativeLayout childAt = (RelativeLayout) getChildAt(mSelectedTab);
            int left = childAt.getChildAt(0).getLeft();
            int right = childAt.getChildAt(0).getRight();
            int scrollx = mCurrentScroll - getWidth() * mSelectedTab;
            if (scrollx < 0) {
                int scaleX = getWidth() / getCurrentLeftViewWidth(mSelectedTab);
                int viewChildLeft = getViewChildLeft(mSelectedTab);
                int viewChildRight = getViewChildRight(mSelectedTab);
                int lastChildLeft = getViewChildLeft(mSelectedTab - 1);
                int lastChildRight = getViewChildRight(mSelectedTab - 1);
                rightX = viewChildRight;
                int lineX = viewChildLeft + scrollx * scaleX;
                leftX = lineX;
                if (leftX < lastChildLeft) {
                    leftX = lastChildLeft;
                    rightX = Math.max(viewChildLeft + lineX - lastChildLeft, lastChildRight);
                }
            } else if (scrollx > 0) {
                //手指滑动像素
                int scaleX = getWidth() / getCurrentViewWidth(mSelectedTab + 1);
                int viewChildRight = getViewChildRight(mSelectedTab);
                int nextChildRight = getViewChildRight(mSelectedTab + 1);
                int nextChildLeft = getViewChildLeft(mSelectedTab + 1);
                int viewChildLeft = getViewChildLeft(mSelectedTab);
                int lineX = viewChildRight + scrollx * scaleX;
                leftX = viewChildLeft;
                rightX = lineX;
                if (rightX > nextChildRight) {
                    rightX = nextChildRight;
                    leftX = Math.min(viewChildLeft + lineX - nextChildRight, nextChildLeft);
                }
            } else {
                // 区分第一个子View的区别，无需add
                if (mSelectedTab > 0) {
                    int itemWidthAll = 0;
                    for (int i = 0; i < mSelectedTab; i++) {
                        itemWidthAll += getChildAt(i).getWidth();
                        leftX = itemWidthAll + left;
                        rightX = itemWidthAll + right;
                    }
                } else {
                    leftX = left;
                    rightX = right;
                }
            }
            Log.e(TAG, " leftX " + leftX + "   scrollx  " + scrollx + " mCurrentScroll " + mCurrentScroll + " mSelectedTab " + mSelectedTab);
        } else {
            leftX = mSelectedTab * cursorWidth + scroll_x + mItemLeft;
            rightX = (mSelectedTab) * cursorWidth + scroll_x + mItemRight;
        }
        float bottomY = getHeight() - linePaddingBottom;
        float topY = bottomY - mFooterLineHeight;
        drawLineRect.left = leftX;
        drawLineRect.right = rightX;
        drawLineRect.bottom = bottomY;
        drawLineRect.top = topY;
        int roundXY = isRoundRectangleLine ? (mFooterLineHeight / 2) : 0;
        canvas.drawRoundRect(drawLineRect, roundXY, roundXY, mPaintFooterLine);
    }

    private void onScrolled(int h) {
        mCurrentScroll = h;
        invalidate();
    }

    public int getViewWidth(int posi) {
        int beforeViewWidth = getBeforeViewWidth(posi);
        if (posi < getChildCount()) {
            return getWidth();
        }
        return getChildAt(posi).getWidth() + beforeViewWidth;
    }

    public int getCurrentViewWidth(int posi) {
        int viewWidth = 0;
        if (posi >= getChildCount()) {
            return viewWidth;
        }
        for (int i = 0; i <= posi; i++) {
            View child = getChildAt(i);
            viewWidth += child.getWidth();
        }
        return viewWidth;
    }

    public int getCurrentLeftViewWidth(int posi) {
        int viewWidth = 0;
        if (posi >= getChildCount()) {
            return viewWidth;
        }
        for (int i = posi; i >= 0; i--) {
            View child = getChildAt(i);
            viewWidth += child.getWidth();
        }
        return viewWidth;
    }

    public int getBeforeViewWidth(int posi) {
        int viewWidth = 0;
        if (posi >= getChildCount()) {
            return viewWidth;
        }
        for (int i = 0; i < posi; i++) {
            View child = getChildAt(i);
            viewWidth += child.getWidth();
        }
        return viewWidth;
    }

    public int getViewHalf(int posi) {
        if (posi >= getChildCount()) {
            return 0;
        }
        int beforeViewWidth = getBeforeViewWidth(posi);
        int half = getChildAt(posi).getWidth() / 2;
        return half + beforeViewWidth;
    }

    public int getViewChildLeft(int posi) {
        if (posi >= getChildCount()) {
            return 0;
        }

        int beforeViewWidth = getBeforeViewWidth(posi);
        RelativeLayout child = (RelativeLayout) getChildAt(posi);
        int viewLeft = beforeViewWidth + child.getChildAt(0).getLeft();
        return viewLeft;
    }

    public int getViewChildRight(int posi) {
        if (posi >= getChildCount()) {
            return 0;
        }
        int beforeViewWidth = getBeforeViewWidth(posi);
        RelativeLayout child = (RelativeLayout) getChildAt(posi);
        int viewRight = beforeViewWidth + child.getChildAt(0).getRight();
        return viewRight;
    }


    public synchronized void onSwitched(int position) {
        if (mSelectedTab == position) {
            return;
        }
        setCurrentTab(position);

    }

    /**
     * init indication
     *
     * @param startPosition init select pos
     * @param tabs          title list
     * @param mViewPager    ViewPage
     */
    public void init(int startPosition, List<TitleInfo> tabs, ViewPager mViewPager) {
        removeAllViews();
        this.mSelectedTab = startPosition;
        this.mViewPager = mViewPager;
        this.mViewPager.addOnPageChangeListener(this);
        this.mTitles = tabs;
        this.mItemCount = tabs.size();
        setWeightSum(mItemCount);
        if (mSelectedTab > tabs.size()) {
            mSelectedTab = tabs.size();
        }
        for (int i = 0; i < mItemCount; i++) {
            add(tabs.get(i).getName(), i);
        }
        mViewPager.setCurrentItem(mSelectedTab);
        invalidate();
        requestLayout();
    }

    /**
     * init indication
     *
     * @param startPosition init select pos
     * @param tabs          title list
     * @param mViewPager    ViewPage
     * @param mViewPager    hasPic
     */
    public void init(int startPosition, List<TitleInfo> tabs, ViewPager mViewPager, boolean hasPic) {
        removeAllViews();
        this.mSelectedTab = startPosition;
        this.mViewPager = mViewPager;
        this.mViewPager.addOnPageChangeListener(this);
        this.mTitles = tabs;
        this.mItemCount = tabs.size();
        this.mHasPic = hasPic;
        setWeightSum(mItemCount);
        if (mSelectedTab > tabs.size()) {
            mSelectedTab = tabs.size();
        }
        for (int i = 0; i < mItemCount; i++) {
            add(tabs.get(i).getName(), i, tabs.get(i).getDrawableId());
        }
        mViewPager.setCurrentItem(mSelectedTab);
        invalidate();
        requestLayout();
    }


    protected void add(String label, int position) {
        TextView view = new TextView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(params);
        if (textCenterFlag == LINE_CENTER) {
            view.setPadding(0, 0, 0, linePaddingBottom);
        }
        view.setText(label);
        setTabTextSize(view, position == mSelectedTab);
        view.setId(BASE_ID + position);
        view.setOnClickListener(this);
        addView(view);
    }

    protected void add(String label, int position, int drawable) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.setLayoutParams(params);
        TextView view = new TextView(getContext());
        LayoutParams params_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(params_text);
        relativeLayout.addView(view);

        if (drawable != 0) {
            Drawable drawable_myrecord = getResources().getDrawable(
                    drawable);
            drawable_myrecord.setBounds(0, 0, 39, 39);

            view.setCompoundDrawables(drawable_myrecord, null, null, null);

            view.setCompoundDrawablePadding(21);
        }

        if (textCenterFlag == LINE_CENTER) {
            view.setPadding(0, 0, 0, linePaddingBottom);
        }
        view.setText(label);
        setTabTextSize(view, position == mSelectedTab);
        view.setId(BASE_ID + position);
        view.setOnClickListener(this);
        addView(relativeLayout);
    }

    @Override
    public void onClick(View v) {
        int position = v.getId() - BASE_ID;
        isNewClick = true;
        mViewPager.setCurrentItem(position, true);
        if (onItemDoubleClickListener != null) {
            if (NoDoubleClickUtils.isDoubleClick() && lastPosition == position) {
                onItemDoubleClickListener.setOnItemDoubleClickListener(position);
            } else {
                lastPosition = position;
            }
        }
    }

    /**
     * get title list size
     *
     * @return list size
     */
    public int getTitleCount() {
        return mTitles != null ? mTitles.size() : 0;
    }

    public synchronized void setCurrentTab(int index) {
        if (index < 0 || index >= getTitleCount()) {
            return;
        }
        View oldTab = getChildAt(mSelectedTab);
        if (oldTab != null) {
            oldTab.setSelected(false);
            setTabTextSize(oldTab, false);
        }

        mSelectedTab = index;
        View newTab = getChildAt(mSelectedTab);
        if (newTab != null) {
            setTabTextSize(newTab, true);
            newTab.setSelected(true);
        }


    }

    /**
     * set select textView textSize&textColor state
     *
     * @param tab      TextView
     * @param selected is Select
     */
    private void setTabTextSize(View tab, boolean selected) {
        if (tab instanceof TextView) {
            TextView tv = (TextView) tab;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? mTextSizeSelected : mTextSizeNormal);
            tv.setTextColor(selected ? mTextColorSelected : mTextColorNormal);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mCurrentScroll == 0 && mSelectedTab != 0) {
            mCurrentScroll = (getWidth() + mViewPager.getPageMargin()) * mSelectedTab;
        }
    }


    /**
     * title
     */
    public static class TitleInfo {
        String name;
        int drawableId;

        public int getDrawableId() {
            return drawableId;
        }

        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }

        public TitleInfo(String name) {
            this.name = name;
        }

        public TitleInfo(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public CaterpillarIndicator.onItemDoubleClickListener getOnItemDoubleClickListener() {
        return onItemDoubleClickListener;
    }

    public CaterpillarIndicator setOnItemDoubleClickListener(CaterpillarIndicator.onItemDoubleClickListener onItemDoubleClickListener) {
        this.onItemDoubleClickListener = onItemDoubleClickListener;
        return this;
    }
}
