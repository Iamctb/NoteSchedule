package com.example.note_schedule_2.LayoutHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

//自定义的GridView，用以显示课程格子
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public MyGridView(Context context){
        super(context);
    }

    public MyGridView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    // 其中onMeasure函数决定了组件显示的高度与宽度；
    // MeasureSpec.makeMeasureSpec函数中第一个参数指布局空间的大小，第二个参数是布局模式
    // MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
    // https://blog.csdn.net/qq_20785431/article/details/51043799(详文)

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec= View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }

}
