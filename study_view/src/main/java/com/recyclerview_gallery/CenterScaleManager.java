package com.recyclerview_gallery;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by GuoXu on 2020/11/30 16:45.
 */
public class CenterScaleManager extends RecyclerView.LayoutManager {


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
