package com.recyclerview_gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.customRecyclerView.ListAdapter;
import com.sn.study_pic.R;
import com.view.lib.xbr.Xbrz;
import com.view.lib.xbr.tool.ScalerTool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/5 16:50
 * @类描述 ${TODO}主界面
 */
public class SpeedRecyclerMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardScaleHelper mCardScaleHelper;
    private ArrayList<String> strings = new ArrayList<>();
    private int[] mPics = new int[]{R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4,
            R.mipmap.icon_5, R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Button button = (Button) findViewById(R.id.button2);
        initRecyclerView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }
//    new int[]

    private int[] destPixels = new int[5820420 * 4];

    public static int[] btoi(byte[] btarr) {
        if (btarr.length % 4 != 0) {
            return null;
        }
        int[] intarr = new int[btarr.length / 4];

        int i1, i2, i3, i4;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4)//j循环int		k循环byte数组
        {
            i1 = btarr[k];
            i2 = btarr[k + 1];
            i3 = btarr[k + 2];
            i4 = btarr[k + 3];

            if (i1 < 0) {
                i1 += 256;
            }
            if (i2 < 0) {
                i2 += 256;
            }
            if (i3 < 0) {
                i3 += 256;
            }
            if (i4 < 0) {
                i4 += 256;
            }
            intarr[j] = (i1 << 24) + (i2 << 16) + (i3 << 8) + (i4 << 0);//保存Int数据类型转换

        }
        Log.e("getByteArrayToIntArray", intarr.length + "");
        return intarr;
    }

    private int[] src = new int[3264 * 2448];
    private int[] out = new int[3264 * 2448 * 36];

    private void test() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String input = externalStorageDirectory.getPath() + "/cnsj.jpg";
        String output = externalStorageDirectory.getPath() + "/cnsj.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(input);
        bitmap.getPixels(src, 0, 3264, 0, 0, 3264, 2448);
        Xbrz.scaleImage(6, false, src, out, 3264, 2448);
        // You are using RGBA that's why Config is ARGB.8888
        bitmap = Bitmap.createBitmap(3264, 2448, Bitmap.Config.ARGB_8888);
        // vector is your int[] of ARGB
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(out));
        Log.e("bitmap", bitmap.toString());
//        byte[] data = null;
//        try {
//            File file = new File(input);
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int len = -1;
//            while ((len = fis.read(b)) != -1) {
//                bos.write(b, 0, len);
//            }
//            data = bos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Xbrz.scaleImage(2, false, btoi(data), destPixels, 3264, 2448);
//        getFileFromBytes(itob(destPixels), output);
    }

    public static byte[] itob(int[] intarr) {
        int bytelength = intarr.length * 4;//长度
        byte[] bt = new byte[bytelength];//开辟数组
        int curint = 0;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4) {
            curint = intarr[j];
            bt[k] = (byte) ((curint >> 24) & 0b1111_1111);//右移4位，与1作与运算
            bt[k + 1] = (byte) ((curint >> 16) & 0b1111_1111);
            bt[k + 2] = (byte) ((curint >> 8) & 0b1111_1111);
            bt[k + 3] = (byte) ((curint >> 0) & 0b1111_1111);
        }


        return bt;
    }

    //将byte[]替换为int[]
    private int[] getByteArrayToIntArray(byte[] byteArray) {
        int[] intArray;
        intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0x80) == 0) {
                intArray[i] = (int) byteArray[i];
            } else {
                intArray[i] = (((int) byteArray[i]) & 0x0ff);
            }
        }

        return intArray;
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(new ScaleLayoutManager(this, Util.Dp2px(this, 10)));
        ListAdapter adapter = new ListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        for (int i = 0; i < 10; i++) {
            strings.add("标签 " + i);
        }
        adapter.setData(strings);
        // mRecyclerView绑定scale效果
//        mCardScaleHelper = new CardScaleHelper();
//        mCardScaleHelper.setCurrentItemPos(1);
//        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
    }
}
