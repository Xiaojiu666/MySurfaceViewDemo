package com.view.recycleView

import android.app.Service
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sn.study_pic.R
import com.view.BaseDataActivity
import com.view.StickyScrollView.ListAdapter
import com.view.recycleView.dragView.OnRecyclerItemClickListener
import com.view.recycleView.dragView.VerticalDragCallBack


class RcvActivity : BaseDataActivity() {
    private var mRecyclerView: RecyclerView? = null

    override fun initView(inflate: View) {
        val listAdapter = ListAdapter(baseContext)
        mRecyclerView = findViewById<View>(R.id.rcy_list) as RecyclerView
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.adapter = listAdapter
        listAdapter.setData(arrayListData)
        val verticalDragCallBack = VerticalDragCallBack(arrayListData)
        val itemTouchHelper = ItemTouchHelper(verticalDragCallBack)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView!!.addOnItemTouchListener(object : OnRecyclerItemClickListener(mRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                Toast.makeText(baseContext, arrayListData.get(vh.layoutPosition).toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                //判断被拖拽的是否是前两个，如果不是则执行拖拽
                if (vh.layoutPosition != 0 && vh.layoutPosition != 1) {
                    itemTouchHelper.startDrag(vh)
                    //获取系统震动服务
//                    val vib: Vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator //震动70毫秒
//                    vib.vibrate(70)
                }
            }
        })
    }

    override fun setLayout(): Int {
        return R.layout.activity_rcv;
    }
}