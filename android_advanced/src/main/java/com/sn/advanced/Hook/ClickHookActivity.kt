package com.sn.advanced.Hook

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sn.advanced.R
import com.sn.advanced.guideActivity.MainActivity
import kotlinx.android.synthetic.main.activity_hook_click.*

/**
 * Created by GuoXu on 2020/11/16 17:44.
 */
class ClickHookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook_click)
        button.setOnClickListener {
            Toast.makeText(this, "别点啦，再点我咬你了...", Toast.LENGTH_SHORT).show();
        }
        HookSetOnClickListenerHelper.hook(this, button)
    }
}