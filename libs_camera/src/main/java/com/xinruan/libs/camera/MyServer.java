package com.xinruan.libs.camera;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Created by GuoXu on 2020/9/16 16:00.
 */
public class MyServer extends AccessibilityService {



    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(getBaseContext(), "O(∩_∩)O~~\r\n红包锁定中...", 1).show();
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(getBaseContext(), "(；′⌒`)\r\n红包功能被迫中断", 1).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "(；′⌒`)\r\n红包功能已关闭", 1).show();
//        Utils.toast("%>_<%\r\n红包功能已关闭");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //通知栏,打开红包
        switch (event.getEventType()) {//先判断是否是通知栏红包和转圈圈界面,这两个任何状态都会去点击
            //第一步：监听通知栏消息,拦截通知的红包
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                for (CharSequence text : event.getText()) {
                    String content = text.toString();
                    //收到红包提醒
                    if (content.contains("[微信红包]") || content.contains("[QQ红包]")) {
                        Toast.makeText(getBaseContext(), "\r\n微信红包", 1).show();
                        //模拟打开通知栏消息,打开后会有新的广播进入微信或者qq
                        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
//                            MyServer.pingUnLock();//开屏,打开屏幕
                            final PendingIntent contentIntent = ((Notification) event.getParcelableData()).contentIntent;
                            try {
                                contentIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                            perforGlobalBACK(1000);

                            //延时的handler(因为开屏有动画)
//                            TimeUtil.mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//
//                                    } catch (PendingIntent.CanceledException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, 500);
                        }
                        break;
                    }
                }
                break;
        }
    }
    /* 全局Home键 */
    public static void perforGlobalBACK(long delay) {
        execShellCmd("input keyevent " + KeyEvent.KEYCODE_BACK);
    }
    /* 全局返回 */
    public static void perforGlobalHome(long delay) {
        execShellCmd("input keyevent " + KeyEvent.KEYCODE_HOME);
    }

    /* 执行Shell命令 */
    public static void execShellCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    // 公共方法


//    /**
//     * 辅助功能是否启动
//     */
//    public static boolean isStart() {
//        return mService != null;
//    }

}
