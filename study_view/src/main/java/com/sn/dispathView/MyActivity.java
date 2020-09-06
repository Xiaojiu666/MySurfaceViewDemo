package com.sn.dispathView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sn.study_pic.R;

/**
 * Created by GuoXu on 2020/8/20 19:54.
 */
public class MyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        MyViewGroup myViewGroup = (MyViewGroup) findViewById(R.id.my_view_group);
        MyView myView = (MyView) findViewById(R.id.my_view);
        myViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "myViewGroup onClick");
            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "myView onClick");
            }
        });
    }

    private static final String TAG = "dispathView";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyActivity : dispatchTouchEvent");
//        Log.e(TAG, "MyActivity dispatchTouchEvent event getAction:  " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MyActivity :  onTouchEvent " );
//        Log.e(TAG, "MyActivity onTouchEvent event getAction:  " + ev.getAction());
        return super.onTouchEvent(ev);
    }
}
