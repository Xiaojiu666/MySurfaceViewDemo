package com.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by GuoXu on 2020/11/4 11:55.
 */
abstract class BaseDataActivity : AppCompatActivity() {

    var arrayListData: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())
        initdata()
        initView(LayoutInflater.from(baseContext).inflate(setLayout(), null))
    }

    abstract fun initView(view: View)

    fun initdata() {
        for (i: Int in 0..9) {
            arrayListData.add("$i")
        }
    }

    abstract fun setLayout(): Int
}