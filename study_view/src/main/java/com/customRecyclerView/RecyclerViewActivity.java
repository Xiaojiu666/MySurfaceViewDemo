package com.customRecyclerView;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sn.study_pic.R;

/**
 * Created by GuoXu on 2020/10/19 9:33.
 */
public class RecyclerViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        RecyclerView re = (RecyclerView) findViewById(R.id.rcy_list);
        re.setLayoutManager(new BannerLayoutManager(getBaseContext(), BannerLayoutManager.VERTICAL));
        ListAdapter listAdapter = new ListAdapter(getBaseContext());
        re.setAdapter(listAdapter);
    }
}
