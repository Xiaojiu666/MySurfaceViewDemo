package com.yuntongxun.mysurfaceviewdemo;

public class BulletChatContentInfo {

    public int getBulletChatXposi() {
        return bulletChatXposi;
    }

    public void setBulletChatXposi(int bulletChatXposi) {
        this.bulletChatXposi = bulletChatXposi;
    }

    public int getBulletChatYposi() {
        return bulletChatYposi;
    }

    public void setBulletChatYposi(int bulletChatYposi) {
        this.bulletChatYposi = bulletChatYposi;
    }

    @Override
    public String toString() {
        return "BulletChatContentInfo{" +
                "bulletChatXposi=" + bulletChatXposi +
                ", bulletCharMessage='" + bulletCharMessage + '\'' +
                ", getBulletChatXMoveSpeed=" + getBulletChatXMoveSpeed +
                ", bulletChatYposi=" + bulletChatYposi +
                '}';
    }

    public int bulletChatXposi = 0;
    private String bulletCharMessage;

    private int getBulletChatXMoveSpeed;

    //public int bulletChatYposi = (int) ((Math.random() * (42 - 1) + 1));
    public int bulletChatYposi = (int) ((Math.random() * (42 - 1) + 1));

    public int getGetBulletChatXMoveSpeed() {
        return getBulletChatXMoveSpeed;
    }

    public void setGetBulletChatXMoveSpeed(int getBulletChatXMoveSpeed) {
        this.getBulletChatXMoveSpeed = getBulletChatXMoveSpeed;
    }


    public String getBulletCharMessage() {
        return bulletCharMessage;
    }

    public void setBulletCharMessage(String bulletCharMessage) {
        this.bulletCharMessage = bulletCharMessage;
    }



}
