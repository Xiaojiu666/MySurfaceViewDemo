package com.sn.study_pic;

import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class StudyAcitivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomDrawableView customDrawableView = new CustomDrawableView(this);
        setContentView(customDrawableView);

        //setContentView(R.layout.activity_study);

        //transitionDrawable();

    }


    /**
     * 渐变两张图片
     */
    private void transitionDrawable() {
        Resources res = getResources();
        TransitionDrawable transition =
                (TransitionDrawable) ResourcesCompat.getDrawable(res, R.drawable.expand_collapse, null);

        ImageView image = (ImageView) findViewById(R.id.toggle_image);
        image.setImageDrawable(transition);
        // Description of the initial state that the drawable represents.
        //image.setContentDescription(getResources().getString(R.string.collapsed));
        // Then you can call the TransitionDrawable object's methods.
        transition.startTransition(1000);
    }
}
