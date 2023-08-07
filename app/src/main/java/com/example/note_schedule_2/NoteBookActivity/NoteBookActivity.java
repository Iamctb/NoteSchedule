package com.example.note_schedule_2.NoteBookActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.note_schedule_2.NoteAdapter.MyAdapter;
import com.example.note_schedule_2.NoteDataBase.DBManager;
import com.example.note_schedule_2.NoteModel.Note;
import com.example.note_schedule_2.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class NoteBookActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton addBtn;
    private DBManager dm;
    private List<Note> noteDataList = new ArrayList<>();
    private MyAdapter adapter;
    private ListView listView;
    private TextView emptyListTextView;
    private TextView tvClassName;


    private String className;
    long waitTime = 2000;
    long touchTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_book);
        //读取笔记
        Intent intent=getIntent();
        className=intent.getStringExtra("classname");

        init();
    }

    //初始化
    private void init() {

        tvClassName=(TextView)findViewById(R.id.tv_className);
        tvClassName.setText(className);
        tvClassName.setTextColor(000000);
        //tvClassName.setBackgroundResource(R.drawable.corner_view);
        tvClassName.setAlpha(0.5f);

        //读取数据
        dm = new DBManager(this);
        dm.readFromDB(noteDataList,className);

        listView = (ListView) findViewById(R.id.list);
        addBtn = (FloatingActionButton) findViewById(R.id.add);
        emptyListTextView = (TextView) findViewById(R.id.empty);
        addBtn.setOnClickListener(this);

        //显示是数据
        adapter = new MyAdapter(this, noteDataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new NoteClickListener());
        listView.setOnItemLongClickListener(new NoteLongClickListener());

        setStatusBarColor();
        updateView();
    }

    //空数据更新
    private void updateView() {
        if (noteDataList.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyListTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.GONE);
        }
    }

    //设置状态栏同色
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#ff6cb506"));
    }

    //button单击事件
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        switch (view.getId()) {
            case R.id.add:
               // Intent i=getIntent();
               //className=i.getStringExtra("classname");
                intent.putExtra("classname",className);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
        }
    }

    //listView单击事件
    private class NoteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) view.getTag();
            String noteId = viewHolder.tvId.getText().toString().trim();
            Intent intent = new Intent(NoteBookActivity.this, EditNoteActivity.class);
            intent.putExtra("classname", className);
            intent.putExtra("id", Integer.parseInt(noteId));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    //listView长按事件
    private class NoteLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
            final Note note = ((MyAdapter) adapterView.getAdapter()).getItem(i);
            if (note == null) {
                return true;
            }
            final int id = note.getId();
            new MaterialDialog.Builder(NoteBookActivity.this)
                    .content("你确定要删除该笔记吗")
                    .positiveText("删除")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                                  @Override
                                  public void onPositive(MaterialDialog dialog) {
                                      DBManager.getInstance(NoteBookActivity.this).deleteNote(id);
                                      adapter.removeItem(i);
                                      updateView();
                                  }
                              }
                    ).show();

            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_clean:
                new MaterialDialog.Builder(NoteBookActivity.this)
                        .content(R.string.are_you_sure)
                        .positiveText(R.string.clean)
                        .negativeText(R.string.cancel)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                for (int id = 0; id < 100; id++)
                                    DBManager.getInstance(NoteBookActivity.this).deleteNote(id);
                                adapter.removeAllItem();
                                updateView();
                            }
                        }).show();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //按返回键时
    public void onBackPressed() {
        finish();
        /*
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
        */
    }
}
