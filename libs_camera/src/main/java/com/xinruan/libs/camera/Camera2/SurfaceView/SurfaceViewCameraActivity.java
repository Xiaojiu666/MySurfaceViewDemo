package com.xinruan.libs.camera.Camera2.SurfaceView;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xinruan.libs.camera.Camera2.TexttureView.AutoFitTextureView;
import com.xinruan.libs.camera.Camera2.Camera2Manager;
import com.xinruan.libs.camera.R;

public class SurfaceViewCameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private Camera2Manager camera2Manager;
    private Size previewSize = new Size(3000,4000);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        AutoFitSurfaceView textureView = (AutoFitSurfaceView) findViewById(R.id.textureView);
        Button button = (Button) findViewById(R.id.button);
        initSurfaceView(textureView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2Manager.switchCamera();
            }
        });
    }

    private void initSurfaceView(final AutoFitSurfaceView surfaceView) {
        surfaceView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {

            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera2Manager = new Camera2Manager(SurfaceViewCameraActivity.this);
                camera2Manager.setPreviewSurface(holder);
                camera2Manager.setCameraId("2");
                camera2Manager.openCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

}
