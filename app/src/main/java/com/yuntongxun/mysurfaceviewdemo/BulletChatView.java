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
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import static android.content.ContentValues.TAG;

public class BulletChatView extends SurfaceView implements Runnable, SurfaceHolder.Callback, BulletChatManager {

    public static final String TAG = "BulletChatView";

    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint, paintClear;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private List<BulletChatContentInfo> bulletChatContentInfos;
    private int maxLines = 10;
    private int memberSize = 100;

    private int viewHeigth;

    private int viewWidth;

    private boolean isStart;

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
        setZOrderOnTop(true);
        holder.addCallback(this);
        Surface surface = holder.getSurface();
        holder.setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
        paint = new Paint();
        paintClear = new Paint();
        paintClear.setColor(Color.BLACK);
        paintClear.setStrokeWidth(200);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        initData();
    }


    private void initData() {
        bulletChatContentInfos = new ArrayList<>();
        for (int i = 0; i <= memberSize; i++) {
            BulletChatContentInfo bulletChatContentInfo = new BulletChatContentInfo();
            bulletChatContentInfo.setBulletCharMessage("郭旭好帅" + i);
            bulletChatContentInfo.setGetBulletChatXMoveSpeed(getGetBulletChatXMoveSpeed());
            bulletChatContentInfos.add(bulletChatContentInfo);
        }
        int colu = (int) Math.ceil((double) bulletChatContentInfos.size() / maxLines);
        for (int i = 0; i < colu; i++) {
            for (int j = 0; j < maxLines; j++) {
                if ((i * maxLines) + j < bulletChatContentInfos.size()) {
                    BulletChatContentInfo mBulletChatContentInfo = bulletChatContentInfos.get((i * maxLines) + j);
                    // 计算 每个对象的
                    int bulletChatXinitLocal = mBulletChatContentInfo.bulletChatXposi + i * 300;
                    mBulletChatContentInfo.setBulletChatXposi(bulletChatXinitLocal);
                }
            }
        }
    }

    private int getGetBulletChatXMoveSpeed() {
        return (int) (Math.random() * (6 - 1) + 1);
    }


    @Override
    public void run() {
        while (true) {
            if (isStart) {
                draw();
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }


    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawBulletText(canvas);
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawBulletText(Canvas canvas) {
        if (canvas != null && bulletChatContentInfos != null && bulletChatContentInfos.size() > 0) {
            for (int i = 0; i < bulletChatContentInfos.size(); i++) {
                BulletChatContentInfo mBulletChatContentInfo = bulletChatContentInfos.get(i);
                if (mBulletChatContentInfo.getBulletChatXposi() < viewWidth )
                    canvas.drawText(mBulletChatContentInfo.getBulletCharMessage(),
                            mBulletChatContentInfo.bulletChatXposi += mBulletChatContentInfo.getGetBulletChatXMoveSpeed(),
                            (i % maxLines) * 40 + 40, paint);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initThread();
        viewHeigth = getHeight();
        viewWidth = getWidth();
    }

    private void initThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void startBulletChat() {
        isStart = true;
    }

    @Override
    public void stopBulletChat() {
        isStart = false;
    }

    @Override
    public void setBulletChatMode(BulletChatMode bulletChatMode) {

    }

    @Override
    public void clearView() {
        isStart = false;
        try {
            //这是定义橡皮擦画笔
            Paint clearPaint = new Paint();
            clearPaint.setAntiAlias(true);
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            holder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void setBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList) {
        bulletChatContentInfos = bulletChatContentInfoList;
    }

    @Override
    public void insertBulletChatData(BulletChatContentInfo bulletChatContentInfoList) {
        if (bulletChatContentInfoList == null) {
            return;
        }
        bulletChatContentInfoList.setBulletChatXposi(-30);
        bulletChatContentInfoList.setGetBulletChatXMoveSpeed(getGetBulletChatXMoveSpeed());
        bulletChatContentInfos.add(bulletChatContentInfoList);
    }

    @Override
    public void insertBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList) {

    }
}
