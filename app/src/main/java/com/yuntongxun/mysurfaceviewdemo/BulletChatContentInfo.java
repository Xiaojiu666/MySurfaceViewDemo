package com.yuntongxun.mysurfaceviewdemo;

public class BulletChatContentInfo {

    public int bulletChatXposi = 0;
    public int bulletChatYposi = (int) (Math.random() * 400);

    public String getBulletCharMessage() {
        return bulletCharMessage;
    }

    public void setBulletCharMessage(String bulletCharMessage) {
        this.bulletCharMessage = bulletCharMessage;
    }

    private String bulletCharMessage;

}
