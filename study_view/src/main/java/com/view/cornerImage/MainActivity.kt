package com.view.cornerImage

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sn.study_pic.R

class MainActivity : AppCompatActivity() {


    var mTextView: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
//        mTextView = findViewById<TextView>(R.id.textView);
//        mTextView!!.setOnClickListener { startAnimi() }
//        startAnimi();
//        CornerImage(baseContext);
    }

    fun startAnimi() {
        val translationX: Float = mTextView?.translationX?.toFloat() ?: 0f
        val toFloat = mTextView?.width?.toFloat() ?: 0f
        val scaleX = ObjectAnimator.ofFloat(mTextView, "scaleX", 1f, 2f, 1f);
        val scaleY = ObjectAnimator.ofFloat(mTextView, "scaleY", 1f, 2f, 1f);
        val animatorSet = AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.duration = 2000;
        animatorSet.start();
    }
}