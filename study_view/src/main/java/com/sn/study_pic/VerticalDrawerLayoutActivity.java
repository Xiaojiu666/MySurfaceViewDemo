package com.sn.study_pic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.sn.customView.VerticalDrawerLayout;

/**
 * Created by GuoXu on 2020/8/17 14:35.
 */
public class VerticalDrawerLayoutActivity extends Activity {

    VerticalDrawerLayout mDrawerLayout;
    ImageView mArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drawer);

        mDrawerLayout = (VerticalDrawerLayout) findViewById(R.id.vertical);
        mArrow = (ImageView) findViewById(R.id.img);


        mDrawerLayout.setDrawerListener(new VerticalDrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mArrow.setRotation(slideOffset * 180);
            }
        });
    }

    public void onClick(View v) {
        if (mDrawerLayout.isDrawerOpen()) {
            mDrawerLayout.closeDrawer();
        } else {
            mDrawerLayout.openDrawerView();
        }
    }

}
