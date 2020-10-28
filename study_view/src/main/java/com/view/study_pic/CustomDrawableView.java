package com.view.study_pic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

public class CustomDrawableView extends View {
    private ShapeDrawable drawable;

    public CustomDrawableView(Context context) {
        super(context);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;
//        setContentDescription(context.getResources().getString(
//                R.string.my_view_desc));
        //如果您想动态绘制二维图形，ShapeDrawable 对象是一种不错的选择。您可以程序化地在 ShapeDrawable 对象上绘制基元形状，并采用您的应用所需的样式。
        //
        //ShapeDrawable 是 Drawable 的子类。因此，您可以在需要使用 Drawable 时使用 ShapeDrawable。例如，
        // 您可以使用 ShapeDrawable 对象设置视图的背景，具体方法是将其传递给视图的 setBackgroundDrawable() 方法。您还可以绘制形状作为其自定义视图，然后将其添加到应用的布局中。
        drawable = new ShapeDrawable(new OvalShape());
        // If the color isn't set, the shape uses black as the default.
        drawable.getPaint().setColor(0xff74AC23);
        // If the bounds aren't set, the shape can't be drawn.
        drawable.setBounds(x, y, x + width, y + height);
    }

    protected void onDraw(Canvas canvas) {
        drawable.draw(canvas);
    }

}
