package com.example.note_schedule_2.DataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.note_schedule_2.R;

import java.util.ArrayList;


//顶部 星期 的适配器
public class GvDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;     //建立泛型数组

    public GvDataAdapter(Context context){
        this.context=context;
        list=new ArrayList<>();
        //设置显示数据
        list.add("时间");
        list.add("周一");
        list.add("周二");
        list.add("周三");
        list.add("周四");
        list.add("周五");
        list.add("周六");
        list.add("周日");
    }

    public int getCount(){
        return list!=null ? list.size():0;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    //继承BaseAdapter需要重写以下方法
    //getCount : 要绑定的条目的数目，比如格子的数量
    //getItem（）和getItemId（）一般不怎么用，所以完全不用管
    //getItem : 根据一个索引（位置）获得该位置的对象
    //getItemId : 获取条目的id
    //getView : 获取该条目要显示的界面

    public View getView(int position , View convertView, ViewGroup parent){
        ViewHolder vh=null;
        if(convertView==null){
            vh=new ViewHolder();
            //加载布局
            convertView= LayoutInflater.from(context).inflate(R.layout.item_gv_date,null);
            vh.tvDate=(TextView)convertView.findViewById(R.id.tvDate);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        //给格子里面设计数据
        vh.tvDate.setText(list.get(position));
        return convertView;

        //要知道setTag方法是干什么的，他是给View对象的一个标签，标签可以是任何内容，
        // 我们这里把他设置成了一个对象，因为我们是把vlist2.xml的元素抽象出来成为一
        // 个类ViewHolder，用了setTag，这个标签就是ViewHolder实例化后对象的一个属性。
        // 我们之后对于ViewHolder实例化的对象holder的操作，都会因为java的引用机制而
        // 一直存活并改变convertView的内容，而不是每次都是去new一个。我们就这样达到的重用的目的，
        //使我们的Viewholder对象与convertView结合，达到缓存的效果，提高效率


    }

    //保存资源
    public static final class ViewHolder{
        TextView tvDate;
    }

}
