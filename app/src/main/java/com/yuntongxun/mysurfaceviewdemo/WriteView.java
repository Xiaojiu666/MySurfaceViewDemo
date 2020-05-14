package com.yuntongxun.mysurfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WriteView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private boolean isDrawing;
    private Canvas canvas;
    private Paint paint;
    private Path mPath;

    public WriteView(Context context) {
        super(context);
        initView();
    }

    public WriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        mPath = new Path();
    }

    @Override
    public void run() {
        while (isDrawing) {
            draw();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(mPath, paint);
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
