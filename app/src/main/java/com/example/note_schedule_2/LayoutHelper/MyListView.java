package com.example.note_schedule_2.LayoutHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

//自定义的ListView,用以显示左侧课程节数
public class MyListView extends ListView {
    public MyListView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public MyListView(Context context){
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    // 其中onMeasure函数决定了组件显示的高度与宽度；
    // MeasureSpec.makeMeasureSpec函数中第一个参数指布局空间的大小，第二个参数是布局模式
    // MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
    public void onMeasure(int widthMesureSpec,int heihtMeasureSpec){
        int hw=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMesureSpec,hw);
    }


}
