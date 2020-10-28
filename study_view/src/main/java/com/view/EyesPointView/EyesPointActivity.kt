package com.view.EyesPointView

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.sn.study_pic.R
import com.view.EyesPointView.EyesPointView.OnCircleDrawComplete

/**
 * Created by GuoXu on 2020/10/27 12:31.
 */
class EyesPointActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "EyesPointActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_eyes_point)
        val seekBar = findViewById<View>(R.id.seekBar) as SeekBar
        val top = findViewById<View>(R.id.button) as Button
        val bottom = findViewById<View>(R.id.button2) as Button
        val left = findViewById<View>(R.id.button3) as Button
        val right = findViewById<View>(R.id.button4) as Button
        val eyesPointView = findViewById<View>(R.id.eyesPointView) as EyesPointView
        val function: (View) -> Unit = {
            eyesPointView.moveToTop(1);
        }
        top.setOnClickListener(function)
        bottom.setOnClickListener {
            eyesPointView.moveToBottom(1);
        }
        left.setOnClickListener {
            eyesPointView.moveToLeft(1);
        }
        right.setOnClickListener {
            eyesPointView.moveToRight(1);
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.e("onProgressChanged", "PROGRESS $progress")
                eyesPointView.distancePupil = EyesPointView.DEFAULT_PUPIL_DISTANCE + progress * 3
                eyesPointView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        eyesPointView.setOnCircleDrawComplete(object : OnCircleDrawComplete {
            override fun onDrawComplete() {
                Log.e(TAG, "leftCircles size ${eyesPointView.circleLeftPoints?.size}")
                Log.e(TAG, "rightCircles size ${eyesPointView.circleRightPoints?.size}")
            }
        })
    }


}