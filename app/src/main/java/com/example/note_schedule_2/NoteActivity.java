package com.example.note_schedule_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_schedule_2.DataSave.Coordinate;

public class NoteActivity extends AppCompatActivity {

    private int position;           //位置
    private EditText classNum;      //时长
    private EditText className;     //名称
    private EditText classRoom;     //教室
    private EditText teacher;       //老师
    private EditText testTime;      //考试时间
    private EditText testRoom;      //考试地点
    private Button btnComfire;      //确认

    //private MySchedule mySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        init();

        Intent intent=getIntent();
        //数据类型的转化，得到点击位置
        String mark=intent.getStringExtra("mark");

        if(mark.equals("haveView")){
            Coordinate coordinate=(Coordinate) intent.getSerializableExtra("data_coordinate");
            disPlay(coordinate);
        }else if(mark.equals("noView")){
            String Sdata_position=intent.getStringExtra("position");
            position=Integer.parseInt(Sdata_position);
            saveData();
        }

    }

    public void init(){
        className=(EditText)findViewById(R.id.et_className);
        classNum=(EditText)findViewById(R.id.et_classNum);
        classRoom=(EditText)findViewById(R.id.et_classRoom);
        teacher=(EditText)findViewById(R.id.et_teacher);
        testTime=(EditText)findViewById(R.id.et_testTime);
        testRoom=(EditText)findViewById(R.id.et_testRoom);
        btnComfire=(Button)findViewById(R.id.btn_comfire);
    }

    //如果是已经有了视图，就显示数据
    public void disPlay(Coordinate coordinate){
        className.setText(coordinate.getClassName());
        int num=coordinate.getClassNum();

        classNum.setText(num+"");
        classRoom.setText(coordinate.getClassRoom());
        teacher.setText(coordinate.getTeacher());
        testTime.setText(coordinate.getTestTime());
        testRoom.setText(coordinate.getTestRoom());
    }

    //如果是没有视图，就添加数据
    public void saveData(){
        btnComfire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataClassName=className.getText().toString();
                String dataClassNum=classNum.getText().toString();
                String dataClassRoom=classRoom.getText().toString();
                String dataTeacher=teacher.getText().toString();
                String dataTestTime=testTime.getText().toString();
                String dataTestRoom=testRoom.getText().toString();

                //课程、节数、教室不能为空
                if(!TextUtils.isEmpty(dataClassName) && !TextUtils.isEmpty(dataClassNum) && !TextUtils.isEmpty(dataClassRoom)){
                    int account=Integer.parseInt(dataClassNum);
                    if(account>=1 && account<=13){
                        //创建存储对象
                        Coordinate coordinate=new Coordinate(position,account,dataClassName,dataClassRoom,dataTeacher,dataTestTime,dataTestRoom);
                        //添加视图
                        Intent intent=new Intent(NoteActivity.this,ScheduleActivity.class);
                        intent.putExtra("coordinate",coordinate);
                        setResult(RESULT_OK,intent);
                        //startActivity(intent);
                    }
                }
            }
        });
    }

}
