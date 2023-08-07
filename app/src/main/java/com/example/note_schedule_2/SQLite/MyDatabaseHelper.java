package com.example.note_schedule_2.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by 17631 on 2019/8/27.
 */

//创建用户注册表
public class MyDatabaseHelper extends SQLiteOpenHelper{
    //注册表
     public static final String CREATE_USER_REGISTER="create table UserTable("
            +"account text primary key ,"       //账号   设置主键 只有整数才能使用自动增加关键字
            +"nick text,"                       //昵称
            +"password text,"                   //密码
            +"school text)";                    //学校

    //笔记表
    public static final String CREATE_NOTE="create table NoteTable("
            +"account text primary key,"
            +"word text,"
            +"picture blob)";



    private Context mContext;

    //构造函数，参数依次代表意思为：上下文、数据库名，第三个参数允许我们在查询数据时返回的一个自定义的Curor，
    // 一般传入null即可，第四个参数表示当前数据库的版本号，方便对数据库进行升级操作。
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_REGISTER);
        db.execSQL(CREATE_NOTE);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists UserTable");
        db.execSQL("drop table if exists NoteBook");
        onCreate(db);
    }

    //读取图片
    public Bitmap getpicture(int position){
        SQLiteDatabase db=getReadableDatabase();
        //获取数据库数据
        Cursor cursor=db.query("NoteTable",null,null,null,null,null,null);
        byte[] in=cursor.getBlob(cursor.getColumnIndex("picture"));
        Bitmap bitmap= BitmapFactory.decodeByteArray(in,0,in.length);
        return bitmap;
    }

    //将图片转换成字节
    public byte[] imgToByte(Drawable drawable){
        BitmapDrawable bitmapDrawable=(BitmapDrawable)drawable;
        Bitmap bitmap=bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //插入图片
    public long insertImage(byte[] img){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("picture",img);
        long result=db.insert("NoteTable",null,contentValues);
        db.close();
        return result;
    }



    public Cursor queryUserTable(){
        SQLiteDatabase db=getReadableDatabase();
        //获取所有行
        Cursor cursor=db.query("UserTable",null,null,null,null,null,null);
        return cursor;
    }

    public Cursor queryNoteTable(){
        SQLiteDatabase db=getReadableDatabase();
        //获取所有行
        Cursor cursor=db.query("NoteTable",null,null,null,null,null,null);
        return cursor;
    }



}
