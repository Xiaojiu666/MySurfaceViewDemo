package com.view.StickyScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * Created by GuoXu on 2020/11/3 18:55.
 */
public class TextNestedScrollView extends HorizontalScrollView {
    public TextNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public TextNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TextNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("TextNestedScrollView OnScrollChangeListener", "scrollX " + scrollX + "scrollY" + scrollY);
            }

        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("TextNestedScrollView onScrollChanged", "scrollX " + l + "scrollY" + t);
    }


}
