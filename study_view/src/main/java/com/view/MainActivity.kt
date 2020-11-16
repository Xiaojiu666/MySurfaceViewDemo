package com.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.customRecyclerView.ListAdapter
import com.sn.study_pic.R
import com.view.recycleView.ItemListAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by GuoXu on 2020/11/15 20:15.
 * 免findViewBuyID 插件 apply plugin: 'kotlin-android-extensions'
 */
class MainActivity : AppCompatActivity() {
    private lateinit var itemListAdapter: ItemListAdapter

    var itemName = arrayOf("拖拽View", "粘性View", "")
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