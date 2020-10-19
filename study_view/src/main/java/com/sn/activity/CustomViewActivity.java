package com.sn.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sn.customView.roundprogressview.RoundProgressView;
import com.sn.study_pic.R;

/**
 * Created by GuoXu on 2020/10/10 11:55.
 */
public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        RoundProgressView rpv1 = (RoundProgressView) findViewById(R.id.rpv1);
        RoundProgressView rpv2 = (RoundProgressView) findViewById(R.id.rpv2);
        RoundProgressView rpv3 = (RoundProgressView) findViewById(R.id.rpv3);
        rpv1.setNumerical(1);
        rpv2.setNumerical(75);
        rpv3.setNumerical(100);
    }
}
