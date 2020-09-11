package com.xinruan.libs.camera.Camera2.TexttureView;

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

import com.xinruan.libs.camera.Camera2.Camera2Manager;
import com.xinruan.libs.camera.Camera2.SurfaceView.AutoFitSurfaceView;
import com.xinruan.libs.camera.R;

public class TexttureViewCameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private Camera2Manager camera2Manager;
    private Size previewSize = new Size(3000, 4000);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_textture);
        AutoFitTextureView textureView = (AutoFitTextureView) findViewById(R.id.textureView);
        Button button = (Button) findViewById(R.id.button);
        initTexttureView(textureView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2Manager.switchCamera();
            }
        });
    }

    private void initTexttureView(final AutoFitTextureView textureView) {

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
                camera2Manager = new Camera2Manager(TexttureViewCameraActivity.this);
                camera2Manager.setPreviewSurface(surface);
                camera2Manager.setCameraId("2");
                camera2Manager.openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }
}
