package com.xinruan.libs.camera.Camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xinruan.libs.camera.Camera2.GlsurfaceView.GlViewCameraActivity;
import com.xinruan.libs.camera.Camera2.SurfaceView.SurfaceViewCameraActivity;
import com.xinruan.libs.camera.Camera2.TexttureView.TexttureViewCameraActivity;
import com.xinruan.libs.camera.R;

/**
 * Created by GuoXu on 2020/9/9 16:16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraPerssion();
        Button textureView = (Button) findViewById(R.id.textureView);
        Button surfaceView = (Button) findViewById(R.id.surfaceView);
        Button glview = (Button) findViewById(R.id.glview);
        textureView.setOnClickListener(this);
        surfaceView.setOnClickListener(this);
        glview.setOnClickListener(this);
    }

    /**
     * 权限检查
     *
     * @param mContext
     */
    public void checkCameraPerssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(this);
        } else {

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textureView:
                Intent textureView = new Intent(this, TexttureViewCameraActivity.class);
                startActivity(textureView);
                break;
            case R.id.surfaceView:
                Intent surfaceView = new Intent(this, SurfaceViewCameraActivity.class);
                startActivity(surfaceView);
                break;
            case R.id.glview:
                Intent glview = new Intent(this, GlViewCameraActivity.class);
                startActivity(glview);
                break;
        }
    }
}
