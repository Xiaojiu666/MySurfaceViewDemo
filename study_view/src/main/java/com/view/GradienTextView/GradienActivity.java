package com.view.GradienTextView;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sn.study_pic.R;

/**
 * Created by GuoXu on 2020/10/26 20:20.
 */
public class GradienActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradien);
        //简单使用

        final GradienTextView gradienTextView = (GradienTextView) findViewById(R.id.gradienTextView);
        gradienTextView.start(Orientation.LEFT_TO_RIGHT, 1000);

        //也可以自定义

        gradienTextView.setOrientation(Orientation.LEFT_TO_RIGHT);
        ValueAnimator animator = ValueAnimator.ofFloat(0.0F, 1.0F);
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                gradienTextView.setCurrentProgress(value);
            }
        });
        animator.start();
    }
}
