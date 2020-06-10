package com.sn.study_pic;

import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.sn.customView.MyView;

public class StudyAcitivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CustomDrawableView customDrawableView = new CustomDrawableView(this);
//        setContentView(customDrawableView);

        setContentView(R.layout.activity_study);

        transitionDrawable();

    }


    /**
     * 渐变两张图片
     */
    private void transitionDrawable() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_study);
        Resources res = getResources();
        TransitionDrawable transition =
                (TransitionDrawable) ResourcesCompat.getDrawable(res, R.drawable.expand_collapse, null);

        ImageView image = (ImageView) findViewById(R.id.toggle_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyView myView = new MyView(getBaseContext());
                linearLayout.addView(myView);
                myView.refersh();
            }
        });
        image.setImageDrawable(transition);
        // Description of the initial state that the drawable represents.
        //image.setContentDescription(getResources().getString(R.string.collapsed));
        // Then you can call the TransitionDrawable object's methods.
        transition.startTransition(1000);
    }
}
