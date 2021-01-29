package com.customRecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sn.study_pic.R;

import java.util.ArrayList;


/**
 * Created by GuoXu on 2020/10/19 9:33.
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        RecyclerView re = (RecyclerView) findViewById(R.id.rcy_list);
        re.setLayoutManager(new BannerLayoutManager(getBaseContext(), BannerLayoutManager.HORIZONTAL));
//        re.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayout.HORIZONTAL,false));
        ListAdapter listAdapter = new ListAdapter(getBaseContext());
        re.setAdapter(listAdapter);
        for (int i = 0; i < 10; i++) {
            strings.add("标签 " + i);
        }
        listAdapter.setData(strings);
    }
}
