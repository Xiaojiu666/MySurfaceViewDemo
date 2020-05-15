package com.yuntongxun.mysurfaceviewdemo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class BulletChatView extends SurfaceView implements Runnable, SurfaceHolder.Callback, BulletChatManager {

    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private List<BulletChatContentInfo> bulletChatContentInfos;

    public BulletChatView(Context context) {
        super(context);
        initView();
    }

    public BulletChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BulletChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        initData();
    }

    private void initData() {
        bulletChatContentInfos = new ArrayList<>();
        BulletChatContentInfo bulletChatContentInfo = new BulletChatContentInfo();
        bulletChatContentInfo.setBulletCharMessage("郭旭");
        bulletChatContentInfos.add(bulletChatContentInfo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            draw();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

        }
    };

    private void draw() {
        try {
            canvas = holder.lockCanvas();
//            canvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawColor(Color.WHITE);
            drawBulletText(canvas);
            //canvas.drawPath(mPath, paint);
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawBulletText(Canvas canvas) {
        if (canvas != null && bulletChatContentInfos != null && bulletChatContentInfos.size() > 0) {
            for (BulletChatContentInfo mBulletChatContentInfo :
                    bulletChatContentInfos) {
                canvas.drawText(mBulletChatContentInfo.getBulletCharMessage(), mBulletChatContentInfo.bulletChatXposi += 20, mBulletChatContentInfo.bulletChatYposi, paint);
            }
        }
    }

    private void translate(final Canvas canvas) {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                canvas.translate(1080 - 1080 * currentValue, 200);
            }
        });
        anim.start();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initThread();
    }

    private void initThread() {
        Thread thread = new Thread(this);
        thread.start();
//        mHandlerThread = new HandlerThread("handler Thread");
//        mHandlerThread.start();
//        handler = new Handler(mHandlerThread.getLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                switch (msg.what) {
//
//                }
//            }
//        };
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void startBulletChat() {

    }

    @Override
    public void setBulletChatMode(BulletChatMode bulletChatMode) {

    }

    @Override
    public void clearView() {

    }

    @Override
    public void setBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList) {
        bulletChatContentInfos = bulletChatContentInfoList;
    }

    @Override
    public void insertBulletChatData(BulletChatContentInfo bulletChatContentInfoList) {

    }

    @Override
    public void insertBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList) {

    }
}
