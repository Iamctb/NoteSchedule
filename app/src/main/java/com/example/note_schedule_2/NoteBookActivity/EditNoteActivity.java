package com.example.note_schedule_2.NoteBookActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.note_schedule_2.NoteDataBase.DBManager;
import com.example.note_schedule_2.NoteModel.Note;
import com.example.note_schedule_2.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**

 */
public class EditNoteActivity extends Activity implements View.OnClickListener{
    private EditText titleEt;
    private EditText contentEt;
    private FloatingActionButton saveBtn;
    private TextView tvClassName;
    private int noteID = -1;
    private DBManager dbManager;
    private String className;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent intent=getIntent();
        className=intent.getStringExtra("classname");

        init();
    }

    //初始化
    private void init() {
        //获取课本位置
       // Intent intent=getIntent();
        //className=intent.getStringExtra("classname");

        tvClassName=(TextView)findViewById(R.id.tv_className);
        //tvClassName.setBackgroundResource(R.drawable.corner_view);
        tvClassName.setAlpha(0.5f);
        tvClassName.setText(className);
        tvClassName.setTextColor(000000);

        dbManager = new DBManager(this);
        titleEt = (EditText) findViewById(R.id.note_title);
        contentEt = (EditText) findViewById(R.id.note_content);
        saveBtn = (FloatingActionButton) findViewById(R.id.save);
        saveBtn.setOnClickListener(this);
        //name，defaultValue
        noteID = getIntent().getIntExtra("id", -1);
        if (noteID != -1) {
            showNoteData(noteID);
        }
        setStatusBarColor();
    }

    //设置状态栏同色
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#ff6cb506"));
    }

    //显示更新的数据
    private void showNoteData(int id) {
        Note note = dbManager.readData(id);
        titleEt.setText(note.getTitle());
        contentEt.setText(note.getContent());
        //控制光标
        Spannable spannable = titleEt.getText();
        Selection.setSelection(spannable, titleEt.getText().length());
    }

    public void onClick(View view) {
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        String time = getTime();
      //  Intent intent=getIntent();
       // className=intent.getStringExtra("classname");
        if (noteID == -1) {
            //默认添加
            dbManager.addToDB(className,title, content, time);
        } else {
            //更新
            dbManager.updateNote(className,noteID, title, content, time);
        }
        Intent i = new Intent(EditNoteActivity.this, NoteBookActivity.class);
        i.putExtra("classname", className);
        startActivity(i);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

    //得到时间
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .customView(R.layout.dialog_webview, false)
                        .positiveText(android.R.string.ok)
                        .build();
                WebView webView = (WebView) dialog.getCustomView().findViewById(R.id.webview);
                webView.loadUrl("file:///android_asset/webview.html");
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent(EditNoteActivity.this, NoteBookActivity.class);
        intent.putExtra("classname", className);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

}