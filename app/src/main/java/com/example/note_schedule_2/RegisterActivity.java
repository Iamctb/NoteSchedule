package com.example.note_schedule_2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_schedule_2.SQLite.MyDatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static EditText et_reg_account;
    private static EditText et_reg_nick;
    private static EditText et_reg_password;
    private static EditText et_reg_school;
    private static EditText et_reg_confirm_password;
    private static Button bt_confirm_register;

    //数据库
    protected MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //创建数据库
        dbHelper= new MyDatabaseHelper(RegisterActivity.this,"UserRegister.db",null,2);
        //dbHelper.getWritableDatabase();

        initView();
        initEvent();
    }

    public void initView(){
        et_reg_account=(EditText)findViewById(R.id.et_reg_account);
        et_reg_nick=(EditText)findViewById(R.id.et_reg_nick);
        et_reg_password=(EditText)findViewById(R.id.et_reg_password);
        et_reg_school=(EditText)findViewById(R.id.et_reg_school);
        et_reg_confirm_password=(EditText)findViewById(R.id.et_reg_confirm_password);
        bt_confirm_register=(Button)findViewById(R.id.bt_register);
    }

    //确认注册
    public void initEvent(){
        bt_confirm_register.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_register:
                //注册时，先根据“账号”查询是否已经注册；
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                //查询现有账号是否已被注册
                Cursor cursor=db.query("UserTable",null,null,null,null,null,null);

                if(cursor!=null){
                    //遍历cursor对象
                    if(cursor.moveToFirst()){
                        do{
                            String account=cursor.getString(cursor.getColumnIndex("account"));
                            if(account.equals(et_reg_account.getText().toString())){
                                Toast.makeText(this,"这个账号被已经注册，请更换",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                }

                //确认密码
                if(!et_reg_password.getText().toString().equals(et_reg_confirm_password.getText().toString())){
                    Toast.makeText(this,"两次密码输入不一样，请重新确认",Toast.LENGTH_SHORT).show();
                    return;
                }

                //加载用户注册信息到数据库
                String account=et_reg_account.getText().toString(); 	//账号
                String nick=et_reg_nick.getText().toString();       	//昵称
                String password=et_reg_password.getText().toString();   //密码
                String school=et_reg_school.getText().toString();   	//学校
                //保存到数据库
                db.execSQL("insert into UserTable(account,nick,password,school)values(?,?,?,?)",
                        new String[]{account,nick,password,school});

                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);

                finish();

                break;
                default:
                    break;
        }


    }

}
