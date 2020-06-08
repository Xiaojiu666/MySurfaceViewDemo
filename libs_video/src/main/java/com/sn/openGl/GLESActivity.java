package com.sn.openGl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sn.video.R;

public class GLESActivity extends AppCompatActivity {

    private GLSurfaceView glsfv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gles);
        initView();
    }

    private void initView() {
        glsfv = (GLSurfaceView) findViewById(R.id.glsfv);
    }
}
