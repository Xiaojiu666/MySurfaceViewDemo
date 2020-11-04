package com.view.recycleView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sn.study_pic.R
import com.view.BaseDataActivity
import com.view.StickyScrollView.ListAdapter

class RcvActivity : BaseDataActivity() {
    private var mRecyclerView: RecyclerView? = null

    override fun initView(inflate: View) {
        val listAdapter = ListAdapter(baseContext)
        mRecyclerView = findViewById<View>(R.id.rcy_list) as RecyclerView
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.adapter = listAdapter
        listAdapter.setData(arrayListData)
    }

    override fun setLayout(): Int {
        return R.layout.activity_rcv;
    }
}