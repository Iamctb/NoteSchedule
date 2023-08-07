package com.example.note_schedule_2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.note_schedule_2.SQLite.MyDatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static EditText et_Account;
    private static EditText et_Password;
    private static Button bt_Login;
    private static Button bt_Register;
    private static CheckBox cb_remember_password;

    protected MyDatabaseHelper dbHelper;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verifyStoragePermissions(this);

        //创建数据库对象
        dbHelper= new MyDatabaseHelper(this,"UserRegister.db",null,2);
        initView();
        initEvent();
    }

    //运行时权限，获取读/写权限
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView(){
        et_Account=(EditText)findViewById(R.id.et_account);
        et_Password=(EditText)findViewById(R.id.et_password);
        bt_Login=(Button)findViewById(R.id.bt_login);
        bt_Register=(Button)findViewById(R.id.bt_register);
        cb_remember_password=(CheckBox)findViewById(R.id.cb_remember_password);
    }

    public void initEvent(){
        bt_Register.setOnClickListener(this);
        bt_Login.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_login:
                //检查账号密码
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                //Cursor cursor=db.query("UserTable",null,null,null,null,null,null);
                Cursor cursor=dbHelper.queryUserTable();
                if(cursor!=null){
                    if(cursor.moveToFirst()){
                        do{
                            String account=cursor.getString(cursor.getColumnIndex("account"));
                            String password=cursor.getString(cursor.getColumnIndex("password"));
                            //如果有该账户，则跳转到主界面
                            if(account.equals(et_Account.getText().toString()) && password.equals(et_Password.getText().toString())){
                                Intent intent=new Intent(this, ScheduleActivity.class);
                                startActivity(intent);
                                return;
                            }
                        }while(cursor.moveToNext());

                        Toast.makeText(this,"请检查账号或密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                cursor.close();

                break;

            case R.id.bt_register:
                Intent intent_register=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_register);
                break;
                default:
                    break;
        }
    }

}
