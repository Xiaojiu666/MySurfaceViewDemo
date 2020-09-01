package com.sn.ExPansionView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sn.study_pic.R;


public class ExpansionItemView extends LinearLayout {

    private int iconImageID;

    public ExpansionItemView(Context context) {
        this(context, null);
    }

    public ExpansionItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ExpansionItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_mine_view, this, true);
        ImageView itemIcon = (ImageView) inflate.findViewById(R.id.item_icon);
        TextView itemTvName = (TextView) inflate.findViewById(R.id.item_tv_name);
        TextView itemTvContant = (TextView) inflate.findViewById(R.id.item_tv_contant);
        ConstraintLayout constraintLayout = (ConstraintLayout) inflate.findViewById(R.id.mine_item_view);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                  //  onClickListener.onClick(v);
                }
            }
        });

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MineItemStyle);
        if (attributes != null) {
            //处理titleBar背景色
            int iconImageID = attributes.getResourceId(R.styleable.MineItemStyle_mine_item_icon, R.drawable.distinguish_scenario);
            String itemName = attributes.getString(R.styleable.MineItemStyle_mine_item_name);
            String itemContant = attributes.getString(R.styleable.MineItemStyle_mine_item_contant);
            itemIcon.setImageResource(iconImageID);
            itemTvName.setText(itemName);
            itemTvContant.setText(itemContant);
            attributes.recycle();
        }
    }


    public interface OnClickListener {
        void onClick(View view);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
