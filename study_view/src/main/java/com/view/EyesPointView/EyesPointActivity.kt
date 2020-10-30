package com.view.EyesPointView

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_B
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.SeekBar
import com.sn.study_pic.R
import com.view.EyesPointView.EyesPointView.OnCircleDrawComplete

/**
 * Created by GuoXu on 2020/10/27 12:31.
 */
class EyesPointActivity : Activity() {
    private lateinit var eyesPointView: EyesPointView

    companion object {
        private const val TAG = "EyesPointActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_view_eyes_point)

        eyesPointView = findViewById<View>(R.id.eyesPointView) as EyesPointView
//        eyesPointView.offsetBean = OffsetUtils.getOffsetData(baseContext)
        eyesPointView.setOnCircleDrawComplete(object : OnCircleDrawComplete {
            override fun onDrawComplete() {
                Log.e(TAG, "leftCircles size ${eyesPointView.circleLeftPoints?.size}")
                Log.e(TAG, "rightCircles size ${eyesPointView.circleRightPoints?.size}")
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e(TAG, "keyCode $keyCode ， keyEvent$event")
        when (keyCode) {
            KeyEvent.KEYCODE_B -> eyesPointView.moveToTop(1)
            KeyEvent.KEYCODE_C -> eyesPointView.moveToBottom(1)
            KeyEvent.KEYCODE_D -> eyesPointView.moveToLeft(1)
            KeyEvent.KEYCODE_E -> eyesPointView.moveToRight(1)
            /*确定*/
            KeyEvent.KEYCODE_F -> eyesPointView.showPoint()
            KeyEvent.KEYCODE_J -> eyesPointView.setPupilDistance(1)
            KeyEvent.KEYCODE_K -> eyesPointView.setPupilDistance(-1)

        }
        return super.onKeyDown(keyCode, event)
    }


}