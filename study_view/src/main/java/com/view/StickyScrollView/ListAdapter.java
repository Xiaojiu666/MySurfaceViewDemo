package com.view.StickyScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sn.study_pic.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RecyclerHolder> {

    private Context mContext;
    private List<String> dataList = new ArrayList<>();

    public ListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_sticky, parent, false);
        return new RecyclerHolder(inflate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, final int position) {
        holder.mItemName.setText("悬浮框 id ；" + dataList.get(position));
//        if (position%2==0){
//            holder.mItemName.setTag(null);
//        }else {
//            holder.mItemName.setTag("sticky");
//        }

//        if (holder.getAdapterPosition() == 0) {
//            holder.mItemName.setTag("sticky");
//            holder.mItemName.setText("悬浮框 id ；" + dataList.get(position));
//        } else {
//            String laseUnitName = dataList.get(holder.getAdapterPosition() - 1);
//            String nowUnitName = dataList.get(position);
//            Log.e("onBindViewHolder", laseUnitName + nowUnitName);
//            if (laseUnitName.equals(nowUnitName)) {
//                holder.mItemName.setTag(null);
//                holder.mItemName.setVisibility(View.GONE);
//            } else {
//                holder.mItemName.setTag("sticky");
//                holder.mItemName.setVisibility(View.VISIBLE);
//                holder.mItemName.setText("悬浮框 id ；" + dataList.get(position));
//            }
//        }
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
        TextView mItemName;

        private RecyclerHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.textView);
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
