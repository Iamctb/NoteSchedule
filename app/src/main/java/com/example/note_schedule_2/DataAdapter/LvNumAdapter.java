package com.example.note_schedule_2.DataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.note_schedule_2.R;

import java.util.ArrayList;

//继承BaseAdapter需要重写以下方法
//getCount : 要绑定的条目的数目，比如格子的数量
//getItem : 根据一个索引（位置）获得该位置的对象
//getItemId : 获取条目的id
//getView : 获取该条目要显示的界面

public class LvNumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String>list;

    public LvNumAdapter(Context context){
        this.context=context;
        list=new ArrayList<>();
        for(int i=1;i<=13;i++){
            list.add(i+"");
        }
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

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder vh=null;
        if(convertView ==null){
            vh=new ViewHolder();
            //引入布局
            convertView= LayoutInflater.from(context).inflate(R.layout.item_lv_num,null);
            vh.tvNum=(TextView)convertView.findViewById(R.id.tvNum);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.tvNum.setText(list.get(position));
        return convertView;
    }

    //新建ViewHolder类，保存资源，提高速率
    public static final class ViewHolder{
        TextView tvNum;
    }

}
