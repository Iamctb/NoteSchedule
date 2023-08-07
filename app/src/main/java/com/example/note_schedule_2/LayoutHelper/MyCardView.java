package com.example.note_schedule_2.LayoutHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyCardView extends TextView {
    private Paint mPaint;       //画笔类
    public MyCardView(Context context, AttributeSet attrs){
        super(context,attrs);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);  //设置画笔的锯齿效果。true代表抗锯齿，false代表不抗锯齿
        mPaint.setStrokeWidth(2);   //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度即宽度。
        mPaint.setAlpha(40);        //设置画笔的Alpha值。范围为0-255。0代表完全透明，255代表完全不透明
    }

    //在onDraw() 方法中对原生控件进行扩展
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth() / 2;
        int y = getMeasuredHeight() / 2;
        int key = getMeasuredWidth()/18;
    }
}
