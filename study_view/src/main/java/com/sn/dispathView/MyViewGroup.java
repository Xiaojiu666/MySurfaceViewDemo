package com.sn.dispathView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by GuoXu on 2020/8/20 19:55.
 */
public class MyViewGroup extends FrameLayout {

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final String TAG = "dispathView";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyViewGroup dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyViewGroup onInterceptTouchEvent");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "MyViewGroup onTouchEvent");
        return super.onTouchEvent(event);
    }
}
