package com.sn.advanced.guideActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sn.advanced.R;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.RecyclerHolder> {

    private Context mContext;
    private List<String> dataList = new ArrayList<>();

    public ItemListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new RecyclerHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, final int position) {
        String name = dataList.get(position);
        holder.mItemName.setText(name);
    }

    public void setData(List<String> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        LinearLayout rootView;
        TextView mItemName;

        private RecyclerHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.item_tv_name);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int posi);
    }
}
