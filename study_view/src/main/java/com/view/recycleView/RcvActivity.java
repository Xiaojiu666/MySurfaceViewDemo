package com.view.recycleView;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sn.study_pic.R;


public class RcvActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcv);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcy_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
