package com.example.note_schedule_2.LayoutHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.note_schedule_2.DataSave.Coordinate;
import com.example.note_schedule_2.NoteBookActivity.NoteBookActivity;
import com.example.note_schedule_2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

// 用于实现添加课程格子效果的自定义View
// ViewGroup是一个可以添加子View的类

public class MySchedule extends ViewGroup {
    private Context context;
    private ArrayList<Coordinate> list;

    //设置存储路径
    private static final String file_path="/schedule.txt";

    //构造函数
    public MySchedule(Context context){
        super(context);
        this.context=context;
        list=new ArrayList<Coordinate>();
    }

    public MySchedule(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;
        list=new ArrayList<Coordinate>();
    }

    //根据List中的课程信息，依次添加TextView;
    protected void onLayout(boolean changed,int l,int t,int r,int b){

        int childNum=getChildCount();
        for(int i=0;i<childNum;i++){
            View child=getChildAt(i);   //获取子View
            Coordinate child_coordinate=list.get(i);    //获取子View的数据
            int position=child_coordinate.getPosition();    //获取ziView的格子地点

            //计算位置
            int hang=position/7; //计算行
            int lie=position%7; //计算列
            int width=getMeasuredWidth()/7;         //计算格子宽度  getMeasureWidth()是计算整个布局的宽度，因为分成了7格，所以除以7
            int height=getMeasuredHeight()/13;       //计算格子高度

            //计算坐标
            int left=lie*width;
            int top=hang*height;
            int right=(lie +1)*width;
            int bottom=(hang+child_coordinate.getClassNum())*height;        //通过课程节数去判断

            //设置布局，要前后左右留一定的间隙
            child.layout(left+5,top+5,right-5,bottom-5);
        }
    }

    //外部调用的、用于添加组件的方法
    public void addToList(Coordinate coordinate){
        list.add(coordinate);
        addView(coordinate);
        save();
    }

    //自定义添加视图
    public void addView(final Coordinate coordinate){
        TextView childTv=new TextView(context);

        childTv.setBackgroundResource(R.drawable.corner_view);  //设置圆角
        GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.corner_view);
        drawable .setColor(randomColor());  //设置背景颜色

        //设置阴影

        childTv.setText(coordinate.getClassName()+"\n"+"@"+coordinate.getClassRoom());      //设置文本
        childTv.setGravity(Gravity.CENTER);     //设置文本居中显示
        childTv.setTextColor(Color.parseColor("#ffffff"));  //设置字体颜色
        childTv.setAlpha(0.8f); //设置透明度

        childTv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(context, NoteBookActivity.class);
                String classname=coordinate.getClassName();
                intent.putExtra("classname",classname);
                /*
                String mark="haveView";
                intent.putExtra("data_coordinate",coordinate);
                intent.putExtra("mark",mark);
                */
                context.startActivity(intent);
            }
        });

        childTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openAlter(coordinate.getPosition(),coordinate.getClassName());      //调用此函数，要么删除该子View,要么就是按错了，取消即可
                return false;
            }
        });

        addView(childTv);       //调用继承方法：添加视图
    }

    //随机生成颜色
    private  int randomColor(){
        Random color=new Random();
        //调用.argb()随机生成颜色：alpha:透明度；之后依次 r,g, b,取值均在0~255
        return Color.argb(255,color.nextInt(256),color.nextInt(256),color.nextInt(256));
    }

    //编辑 dialog
    //https://yiming.blog/article/201602082011.html
    //https://blog.csdn.net/centralperk/article/details/7493917
    private void openAlter(final int position,String text){
        //自定义对话框
        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        //显示对话框
        alertDialog.show();

        //允许输入法弹出：AlertDialog 显示时默认不可以使用输入法。如果需要在弹出的对话框中使用 EditText 、让用户输入内容，需要在执行 show 方法后加入另外两行代码。
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //获取窗口对象，方便进行窗口设置
        Window window=alertDialog.getWindow();
        window.setContentView(R.layout.dialog_edit);        //加载布局

        TextView textView= window.findViewById(R.id.tvContent);
        textView.setText(text); //显示课程名

        Button btnDelete= window.findViewById(R.id.btndelete);
        Button btnCancel= window.findViewById(R.id.btnCancel);

        btnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                removeView(position);   //点击删除，先移除该子View，再关闭窗口
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alertDialog.dismiss();  //按取消，则关闭该窗口
            }
        });

    }

    public void removeView(int position){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getPosition()==position){
                list.remove(i);     //移除对应的数据
                removeView(getChildAt(i));  //删除对应的子View
            }
            save();     //将更新的数据保存在手机中
        }
    }

    //保存信息到手机内存中
    public void save(){
        JSONObject jsonObject=dataCreate(list);     //创造JSON格式的数据
        File sdDir=null;
        //判断手机内存是否存在
        boolean sdExist=Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdExist){
            sdDir=Environment.getExternalStorageDirectory();        //获取更目录
            FileOutputStream fout=null;     //输出流
            try{
                fout=new FileOutputStream(sdDir+file_path);
                byte[] bytes=jsonObject.toString().getBytes();
                //写入
                fout.write(bytes);
                fout.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //把sd卡中的数据读取到list中，然后一一显示
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void read(){
        File sdDir=null;
        //判断sd卡是否存在
        boolean sdCardExist= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdCardExist){
            sdDir=Environment.getExternalStorageDirectory(); //获取根目录
            File file=new File(sdDir+file_path);    //  获取文件实例
            if(!file.exists()){
                return;
            }
            FileInputStream fin=null;
            try {
                fin = new FileInputStream(sdDir + file_path);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                //编码转化
                //https://blog.csdn.net/zhaozheng7758/article/details/6165066
                String res = new String(buffer, StandardCharsets.UTF_8);

                fin.close();

                //数据解析，然后存到list中
                dataParse(res);

                //数据已经全部存储到list中，显示list中的View
                for (int i = 0; i < list.size(); i++) {
                    addView(list.get(i));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //JSON文件的生成，即生成数据
    //输入一个数据数组
    private JSONObject dataCreate(ArrayList<Coordinate> mlist){
        //创造SONObject数据
        JSONObject jsonObject=new JSONObject();
        try{
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<mlist.size();i++){
                JSONObject object=new JSONObject();
                //加载数据
                object.put("position",mlist.get(i).getPosition());
                object.put("classNum",mlist.get(i).getClassNum());
                object.put("className",mlist.get(i).getClassName());
                object.put("classRoom",mlist.get(i).getClassRoom());
                object.put("teacher",mlist.get(i).getTeacher());
                object.put("testTime",mlist.get(i).getTestTime());
                object.put("testRoom",mlist.get(i).getTestRoom());
                //将单组数据放入数组中
                jsonArray.put(i,object);
            }
            //给数组加个名字，然后再放入一个数据中
            jsonObject.put("coordinate",jsonArray);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    // JSON文件的解析
    // https://www.jianshu.com/p/6096e6740ac9
    private void dataParse(String data){
        try{
            //解析出最上一层数据
            JSONObject jsonObject=new JSONObject(data);
            //根据jsonArray数组名 “coordinate”解析出数组
            JSONArray jsonArray=jsonObject.optJSONArray("coordinate");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                Coordinate coordinate=new Coordinate(
                  jsonObject1.optInt("position"),
                  jsonObject1.optInt("classNum"),
                  jsonObject1.optString("className"),
                  jsonObject1.optString("classRoom"),
                        jsonObject1.optString("teacher"),
                        jsonObject1.optString("testTime"),
                        jsonObject1.optString("testRoom")
                );
                list.add(coordinate);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
