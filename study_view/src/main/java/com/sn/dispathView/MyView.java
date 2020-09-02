package com.sn.dispathView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by GuoXu on 2020/8/20 19:56.
 */
public class MyView extends View {
    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final String TAG = "dispathView";
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyView dispatchTouchEvent event getAction:  " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyView onTouchEvent event getAction:  " + ev.getAction());
        return super.onTouchEvent(ev);
    }

}
