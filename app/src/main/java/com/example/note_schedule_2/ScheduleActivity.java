package com.example.note_schedule_2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_schedule_2.DataAdapter.GvContentAdapter;
import com.example.note_schedule_2.DataAdapter.GvDataAdapter;
import com.example.note_schedule_2.DataAdapter.LvNumAdapter;
import com.example.note_schedule_2.DataSave.Coordinate;
import com.example.note_schedule_2.LayoutHelper.MySchedule;

public class ScheduleActivity extends AppCompatActivity {

    private GridView gvDate,gvContent;
    private ListView lvNum;
    private MySchedule mySchedule;


    // private Coordinate coordinate=new Coordinate(0,2,"语文");
    //public  String FileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        init();

        //判断是否有Intent信息传输过来
        getIntentData();

        //打开APP.读取存储的课程信息
        mySchedule.read();


        //因为重叠，所以可以通过GridView的ItemClick获取当前位置信息，
        // 这里传的是position，具体的计算工作交给MySchedule
        gvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                int data_position=position;
                Intent intent=new Intent(ScheduleActivity.this, NoteActivity.class);
                String mark="noView";
                intent.putExtra("position",data_position+"");
                intent.putExtra("mark",mark);
                startActivityForResult(intent,1);
                //openAlter(position);
            }
        });
    }

    private void init(){
        gvDate=(GridView)findViewById(R.id.gv_Data);
        gvDate.setAdapter(new GvDataAdapter(ScheduleActivity.this));

        lvNum=(ListView)findViewById(R.id.lvNum);
        lvNum.setAdapter(new LvNumAdapter(ScheduleActivity.this));

        gvContent=(GridView)findViewById(R.id.gvContent);
        gvContent.setAdapter(new GvContentAdapter(ScheduleActivity.this));

        mySchedule=(MySchedule)findViewById(R.id.mySchedule);
    }

    //添加组件的dialog
    //判断是否有Intent传来
    public void getIntentData(){


        //if(getIntent().getSerializableExtra("coordinate").equals(null)){
            Coordinate coordinate=(Coordinate)getIntent().getSerializableExtra("coordinate");
            if(coordinate!=null){
                mySchedule.addToList(coordinate);
            }

        //}
    }


    private void openAlter(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        //允许编辑
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog);
        final EditText etName = (EditText) window.findViewById(R.id.etName);
        final EditText etNum = (EditText) window.findViewById(R.id.etNum);
        final EditText etRoom=(EditText)window.findViewById(R.id.etRoom);
        Button btnSure = (Button) window.findViewById(R.id.btnSure);
        Button btnCancel = (Button) window.findViewById(R.id.btnCancel);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etNum.getText().toString()) && !TextUtils.isEmpty(etRoom.getText().toString())) {
                    int num = Integer.valueOf(etNum.getText().toString());
                    if (num>=1 && num <=13) {
                        Coordinate coordinate = new Coordinate(position, num, etName.getText().toString(),etRoom.getText().toString(),null,null,null);
                        //此时是新添加View，所以要先添加进列表，然后显示，最后还要进行数据的保存工作，这些全部在addToList中完成了
                        mySchedule.addToList(coordinate);
                        alertDialog.dismiss();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){

            if(resultCode==RESULT_OK){
                Coordinate coordinate = (Coordinate) data.getSerializableExtra("coordinate");
                mySchedule.addToList(coordinate);
            }
        }

    }


}
