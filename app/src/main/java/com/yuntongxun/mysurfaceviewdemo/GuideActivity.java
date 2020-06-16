package com.yuntongxun.mysurfaceviewdemo;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

    private String[] itemTitles = new String[]{"音视频编解码", "弹幕"};


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
                        Intent intent = new Intent(GuideActivity.this, VideoActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intentStudy = new Intent(GuideActivity.this, LiveActivity.class);
                        startActivity(intentStudy);
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < itemTitles.length; i++) {
            strings.add(itemTitles[i]);
        }
        listAdapter.setData(strings);
    }

    private void initView() {
        mGudieRecyclerView = (RecyclerView) findViewById(R.id.guide_rcy);
        mGudieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(mGudieRecyclerView.getContext());
        mGudieRecyclerView.setAdapter(listAdapter);

//        final Button button = (Button) findViewById(R.id.button);
//        button.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ClipData.Item item = new ClipData.Item("ceshi");
//
//                // Create a new ClipData using the tag as a label, the plain text MIME type, and
//                // the already-created item. This will create a new ClipDescription object within the
//                // ClipData, and set its MIME type entry to "text/plain"
//                ClipData dragData = new ClipData(
//                        "ceshi",
//                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
//                        item);
//
//                // Instantiates the drag shadow builder.
//                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(button);
//
//                // Starts the drag
//
//                v.startDrag(dragData,  // the data to be dragged
//                        myShadow,  // the drag shadow builder
//                        null,      // no need to use local data
//                        0          // flags (not currently used, set to 0)
//                );
//                return true;
//            }
//        });
    }


    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth() / 2;

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight() / 2;

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

}
