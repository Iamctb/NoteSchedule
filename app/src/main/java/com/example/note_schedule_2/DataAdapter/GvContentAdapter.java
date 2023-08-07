package com.example.note_schedule_2.DataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.note_schedule_2.LayoutHelper.MyCardView;
import com.example.note_schedule_2.R;
import java.util.ArrayList;

public class GvContentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public GvContentAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
        //13行 7列，设置格子
        for(int i=1;i<=91;i++){
            list.add("");
        }
    }

    //返回格子数目
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
        if(vh==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_gv_content,null);
            vh.mcv=(MyCardView)convertView.findViewById(R.id.mcv);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    public static final class ViewHolder{
        MyCardView mcv;
    }

}
