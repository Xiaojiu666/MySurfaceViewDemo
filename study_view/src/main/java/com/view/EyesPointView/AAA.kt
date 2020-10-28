package com.view.EyesPointView

/**
 * Created by GuoXu on 2020/10/27 16:52.
 */
class AAA {
    private var onCircleDrawComplete: OnCircleDrawComplete? = null

    fun setOnCircleDrawComplete(onCircleDrawComplete: OnCircleDrawComplete?) {
        this.onCircleDrawComplete = onCircleDrawComplete
    }

    interface OnCircleDrawComplete {
        fun onDrawComplete()
    }
}