package com.sn.advanced.guideActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.advanced.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by GuoXu on 2020/11/15 20:15.
 * 免findViewBuyID 插件 apply plugin: 'kotlin-android-extensions'
 */
class MainActivity : AppCompatActivity() {

    private lateinit var itemListAdapter: ItemListAdapter
    var itemName = arrayOf("Hook", "JavaPoet", "反射")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView();
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(baseContext)
        itemListAdapter = ItemListAdapter(baseContext)
        itemListAdapter.setData(itemName.toList())
        recyclerview.adapter = itemListAdapter
    }
}