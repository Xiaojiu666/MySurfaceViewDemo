package com.xinruan.libs.camera.Camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * Created by GuoXu on 2020/9/6 11:20.
 */
public abstract class CameraLoader {


    protected CameraLoader() {
    }
    /**
     * 打开相机
     */
    abstract void openCamera();

    abstract void cloaseCamera();

    abstract void switchCamera();

    abstract int getCameraOrientation();

    abstract Size[] getCameraPreViews();

    abstract void setCameraId(String cameraId);

    abstract List<String> getCameraIds();

    abstract void setPreviewSurface(SurfaceHolder holder);

    abstract void setPreviewSurface(SurfaceTexture surfaceTexture);


}
