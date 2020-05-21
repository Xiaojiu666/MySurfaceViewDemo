package com.yuntongxun.mysurfaceviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BulletChatView bulletChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
    }

    private void initView() {
        Button startBulletChat = (Button) findViewById(R.id.btn_star_bulletchat);
        Button stopBulletChat = (Button) findViewById(R.id.btn_stop_bulletchat);
        Button clearBulletChat = (Button) findViewById(R.id.btn_clear_bulletchat);
        Button addBulletChat = (Button) findViewById(R.id.btn_add_bulletchat);
        bulletChatView = (BulletChatView) findViewById(R.id.bcv_view);
        startBulletChat.setOnClickListener(this);
        stopBulletChat.setOnClickListener(this);
        clearBulletChat.setOnClickListener(this);
        addBulletChat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_star_bulletchat) {
            bulletChatView.startBulletChat();
        } else if (id == R.id.btn_stop_bulletchat) {
            bulletChatView.stopBulletChat();
        } else if (id == R.id.btn_clear_bulletchat) {
            bulletChatView.clearView();
        } else if (id == R.id.btn_add_bulletchat) {
            BulletChatContentInfo bulletChatContentInfo = new BulletChatContentInfo();
            bulletChatContentInfo.setBulletCharMessage("飞机来了。。。。。。。。。。。。。。");
            bulletChatView.insertBulletChatData(bulletChatContentInfo);
        }
    }
}
