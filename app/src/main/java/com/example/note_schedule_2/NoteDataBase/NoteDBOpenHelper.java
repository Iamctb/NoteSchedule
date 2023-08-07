package com.example.note_schedule_2.NoteDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDBOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "NoteTable";
    public static final String ID = "ID";
    public static final String CLASS_NAME="CLASS_NAME";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String TIME = "TIME";
    public static final int VERSION = 1;


    private Context mContext;

    //建立数据库
    public static final String CREATE_NOTE="create table NoteTable("
            +"ID integer primary key autoincrement,"        //笔记id
            +"CLASS_NAME text not  null,"                             //课程名字
            +"CONTENT text,"                                //笔记内容
            +"TITLE text,"                                  //笔记标题
            +"TIME text)";                                  //时间



    //构造函数，参数依次代表意思为：上下文、数据库名，第三个参数允许我们在查询数据时返回的一个自定义的Curor，
    // 一般传入null即可，第四个参数表示当前数据库的版本号，方便对数据库进行升级操作。
    public NoteDBOpenHelper(Context context, String DBname, SQLiteDatabase.CursorFactory factory,int version){
        super(context,DBname,factory,version);
        mContext=context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NOTE);

    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
