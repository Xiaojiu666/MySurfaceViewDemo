package com.yuntongxun.mysurfaceviewdemo.VideoPlayer;

import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sn.libs_video.AudioDecoder;
import com.sn.libs_video.VideoDecoder;
import com.yuntongxun.mysurfaceviewdemo.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoActivity extends AppCompatActivity {

    private SurfaceView sfv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        sfv = (SurfaceView) findViewById(R.id.sfv);
        initPlayer();
    }

    private void initPlayer() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mvtest.mp4";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        //创建视频解码器
        VideoDecoder videoDecoder = new VideoDecoder(path, sfv, null);
        threadPool.execute(videoDecoder);
        videoDecoder.setStateListener();
        //创建音频解码器
        AudioDecoder audioDecoder = new AudioDecoder(path);
        threadPool.execute(audioDecoder);
        audioDecoder.setStateListener();
        //开启播放
        videoDecoder.goOn();
        audioDecoder.goOn();
    }
}
