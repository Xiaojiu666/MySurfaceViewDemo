package com.xinruan.libs.camera.Camera2;

import android.os.Bundle;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xinruan.libs.camera.R;

public class CameraActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        AutoFitTextureView textureView = (AutoFitTextureView) findViewById(R.id.textureView);
        textureView.setAspectRatio(480,640);
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Camera2Manager camera2Manager = new Camera2Manager(this, textureView);
    }
}
