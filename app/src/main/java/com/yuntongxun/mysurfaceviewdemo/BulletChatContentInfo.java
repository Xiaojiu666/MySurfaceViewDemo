package com.yuntongxun.mysurfaceviewdemo;

public class BulletChatContentInfo {

    public int bulletChatXposi = 0;

    public int bulletChatYposi = (int) ((Math.random() * (42 - 1) + 1));

    public int getGetBulletChatXMoveSpeed() {
        return getBulletChatXMoveSpeed;
    }

    public void setGetBulletChatXMoveSpeed(int getBulletChatXMoveSpeed) {
        this.getBulletChatXMoveSpeed = getBulletChatXMoveSpeed;
    }

    private int getBulletChatXMoveSpeed;

    public String getBulletCharMessage() {
        return bulletCharMessage;
    }

    public void setBulletCharMessage(String bulletCharMessage) {
        this.bulletCharMessage = bulletCharMessage;
    }

    private String bulletCharMessage;

}
