package com.yuntongxun.mysurfaceviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuntongxun.mysurfaceviewdemo.VideoPlayer.VideoActivity;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {


    private RecyclerView mGudieRecyclerView;
    private ListAdapter listAdapter;

    private String[] itemTitles = new String[]{"音视频", "11", "22"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initClick();
    }

    private void initClick() {
        listAdapter.setOnItemClick(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int posi) {
                switch (posi) {
                    case 0:
                    default:
                        Intent intent = new Intent(GuideActivity.this, VideoActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            strings.add(itemTitles[i]);
        }
        listAdapter.setData(strings);
    }

    private void initView() {
        mGudieRecyclerView = (RecyclerView) findViewById(R.id.guide_rcy);
        mGudieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(mGudieRecyclerView.getContext());
        mGudieRecyclerView.setAdapter(listAdapter);
    }
}
