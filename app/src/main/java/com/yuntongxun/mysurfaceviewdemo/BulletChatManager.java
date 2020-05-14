package com.yuntongxun.mysurfaceviewdemo;

import java.util.List;

public interface BulletChatManager {

    enum BulletChatMode {
        FULL_VIEW, HALF_VIEW, NONE
    }

    void startBulletChat();
    // 设置弹幕模式
    void setBulletChatMode(BulletChatMode bulletChatMode);

    // 清屏
    void clearView();

    // 设置弹幕数据
    void setBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList);

    // 设置弹幕数据
    void insertBulletChatData(BulletChatContentInfo bulletChatContentInfoList);

    void insertBulletChatData(List<BulletChatContentInfo> bulletChatContentInfoList);
}
