package com.xinruan.libs.camera.Camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by GuoXu on 2020/9/6 11:20.
 */
public abstract class CameraLoader {

    public CameraLoader(Activity mContext) {
       // checkCameraPerssion(mContext);
    }

    protected CameraLoader() {
    }

    /**
     * 权限检查
     *
     * @param mContext
     */
    public void checkCameraPerssion(Activity mContext) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "没有相机权限！", Toast.LENGTH_LONG).show();
        } else {
            requestPermission(mContext);
        }
    }

    //检查和申请权限
    private void requestPermission(Activity mContext) {
        //把需要检查的一组权限放在一个字符串数组里
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        //记录没有获得的权限数量
        //申请未获得的权限（直接把权限组传入）
        ActivityCompat.requestPermissions(mContext, permissions, 1);
    }

    /**
     * 根据指定宽高(预览画面)，打开相机
     * @param width
     * @param height
     */
    abstract void openCamera(int width, int height);

    abstract void cloaseCamera();

    abstract void switchCamera(int Id);

    abstract int getCameraOrientation();

}
