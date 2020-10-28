package com.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class MyViewA extends View {

    public static final String TAG = "MyView";

    public MyViewA(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw");
        super.onDraw(canvas);
    }

    public void refersh() {
        invalidate();
    }
}
