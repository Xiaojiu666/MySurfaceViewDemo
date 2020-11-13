package com.xinruan.libs.camera.Camera2.GlsurfaceView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xinruan.libs.camera.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GuoXu on 2020/9/9 19:03.
 */
public class GlViewCameraActivity extends AppCompatActivity {
    private static String content = "先帝创业未半而中道崩殂，" +
            "今天下三分，" +
            "益州疲弊，" +
            "此诚危急存亡之秋也。" +
            "然侍卫之臣不懈于内，" +
            "忠志之士忘身于外者";
    private static String contentEnglish = "Hey,Robin.";
    private static String contentEnglish1 = "Where is the science museum?";
    private static String contentEnglishNo = "HeyRobin";
    private static String contentNo = "先帝创业未半而中道崩殂" +
            "今天下三分" +
            "益州疲弊" +
            "此诚危急存亡之秋也" +
            "然侍卫之臣不懈于内" +
            "忠志之士忘身于外者";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_glview);
        for (int i = 0; i < contentEnglish1.length(); i++) {

        }
        getEnglishPunc4Content(contentEnglish1);
        Log.e("onCreate", String.valueOf(getEnglishPunc4Content(contentEnglish1)));
    }

    //使用UnicodeScript方法判断
    public static boolean isChineseByScript(char c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Character.UnicodeScript sc = Character.UnicodeScript.of(c);
            if (sc == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }


    public static ArrayList<Integer> getEnglishPunc4Content(String s) {
        ArrayList<Integer> puncs = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-Z]");
        for (int i = 0; i < s.length(); i++) {
            String input = String.valueOf(s.charAt(i));
            Matcher m = p.matcher(input);
            if (!m.matches()) {
                puncs.add(i);
            }
        }
        return puncs;
    }
}
