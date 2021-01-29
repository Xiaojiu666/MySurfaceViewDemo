package com.customRecyclerView;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sn.study_pic.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> dataList = new ArrayList<>();
    private static final int TYPE_FIRST = 0;
    private static final int TYPE_NORMAL = 1;

    public ListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate;
        if (viewType == TYPE_FIRST) {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
            return new ImageRecyclerHolder(inflate);
        } else {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_chapter_details_layout, parent, false);
            return new RecyclerHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String s = dataList.get(position);
        if (holder instanceof ImageRecyclerHolder) {
            ImageRecyclerHolder imageRecyclerHolder = (ImageRecyclerHolder) holder;
        } else {
            RecyclerHolder imageRecyclerHolder = (RecyclerHolder) holder;
            imageRecyclerHolder.mItemName.setText(s);
        }
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIRST;
        } else {
            return TYPE_NORMAL;
        }
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        LinearLayout rootView;
        TextView mItemName;

        private RecyclerHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.item_chapter_details_type_text);
        }
    }

    class ImageRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        private ImageRecyclerHolder(View itemView) {
            super(itemView);
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
