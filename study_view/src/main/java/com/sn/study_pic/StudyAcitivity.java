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

import com.sn.customView.MyViewA;
import com.sn.study_desgin_model.ImageLoader.ImageLoader;

public class StudyAcitivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CustomDrawableView customDrawableView = new CustomDrawableView(this);
//        setContentView(customDrawableView);

        setContentView(R.layout.activity_study);
        ImageView image = (ImageView) findViewById(R.id.toggle_image);
        ImageLoader imageLoader = new ImageLoader();
        //imageLoader.setImageCache(new MemoryCache());
//        imageLoader.setImageCache(new ImageCache() {
//            private String name;
//
//            @Override
//            public void putImageCache(String key, Bitmap bmp) {
//                name = "";
//            }
//
//            @Override
//            public Bitmap getImageCache(String key) {
//                return null;
//            }
//        });
        imageLoader.displayImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591872772964&di=ac60d0540c8ee6756a81e21e51d41dcc&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F031d9d6560dfd1d0000011131877076.jpg", image);

        //transitionDrawable();

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
                MyViewA myView = new MyViewA(getBaseContext());
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
