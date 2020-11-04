package com.view.StickyScrollView

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sn.study_pic.R

/**
 * Created by GuoXu on 2020/11/2 19:16.
 */
class StickScrollViewActivcity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stick_scroll_recyclerview)
        initRecyclerView()
    }

    fun initRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val listAdapter = ListAdapter(baseContext)
        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerview.layoutManager = linearLayoutManager;
        recyclerview.adapter = listAdapter
        recyclerview.isNestedScrollingEnabled = false;
        recyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("initRecyclerView onScrolled", "scrollX " + dx + "scrollY" + dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e("initRecyclerView onScrollStateChanged", "scrollX " + newState)
            }

        })
        val arrayList = ArrayList<String>()
        for (i: Int in 0..9) {
            arrayList.add("$i")
        }
        listAdapter.setData(arrayList)
    }
}