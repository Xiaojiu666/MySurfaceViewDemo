package com.sn.advanced.Hook

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View

/**
 * Created by GuoXu on 2020/11/16 17:59.
 *  点击事件代理类
 */
class ProxyOnClickListener(oriLis: View.OnClickListener) : View.OnClickListener {

    var oriLis: View.OnClickListener? = null


    override fun onClick(view: View?) {
        Log.d("HookSetOnClickListener", "点击事件被hook到了");
        if (oriLis != null) {
            oriLis?.onClick(view);
        }

    }

}