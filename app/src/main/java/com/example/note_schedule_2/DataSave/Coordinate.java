package com.example.note_schedule_2.DataSave;

import java.io.Serializable;

//用于存储位置信息、课程名称、课程时长,教室
//序列化 (Serialization)将对象的状态信息转换为可以存储或传输的形式的过程。
// 在序列化期间，对象将其当前状态写入到临时或持久性存储区。
//简单地说，“序列化”就是将运行时的对象状态转换成二进制，然后保存到流、内存或者通过网络传输给其他端。

public class Coordinate implements Serializable {
    private int position;   //位置
    private int classNum;   //时长
    private String className;       //名称
    private String classRoom;   //教室
    private String teacher; //老师
    private String testTime;    //考试时间
    private String testRoom;    //考试地点

    public Coordinate(int position,int classNum,String className,String classRoom,String teacher,String testTime,String testRoom){
        this.position=position;
        this.className=className;
        this.classNum=classNum;
        this.classRoom=classRoom;
        this.teacher=teacher;
        this.testRoom=testRoom;
        this.testTime=testTime;
    }

    public void setPosition(int position){
        this.position=position;
    }
    public void setClassNum(int classNum){
        this.classNum=classNum;
    }
    public void setClassName(String className){
        this.className=className;
    }
    public void setClassRoom(String classRoom){this.classRoom=classRoom;}
    public void setTeacher(String teacher){
        this.teacher=teacher;
    }
    public void setTestTime(String testTime){
        this.testTime=testTime;
    }
    public void setTestRoom(String testRoom){
        this.testRoom=testRoom;
    }

    public int getPosition(){
        return position;
    }

    public int getClassNum(){
        return classNum;
    }

    public String getClassName(){
        return className;
    }

    public String getClassRoom(){
        return classRoom;
    }

    public String getTeacher(){
        return teacher;
    }
    public String getTestTime(){
        return testTime;
    }
    public String getTestRoom(){
        return testRoom;
    }



}
