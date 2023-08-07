package com.example.note_schedule_2.NoteDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.note_schedule_2.NoteModel.Note;
import java.util.List;

public class DBManager {
    private Context context;
    private NoteDBOpenHelper databaseOpenHelper;
//    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    private static DBManager instance;

    public DBManager(Context context) {
        this.context = context;
        databaseOpenHelper = new NoteDBOpenHelper(context,"Note.db",null,1);
        // 创建and/or打开一个数据库
        //dbReader = databaseOpenHelper.getReadableDatabase();
        dbWriter = databaseOpenHelper.getWritableDatabase();
    }

    //getInstance单例
    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    // 添加数据到数据库
    public void addToDB(String className,String title, String content, String time) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDBOpenHelper.CLASS_NAME,className);
        cv.put(NoteDBOpenHelper.TITLE, title);
        cv.put(NoteDBOpenHelper.CONTENT, content);
        cv.put(NoteDBOpenHelper.TIME, time);
        dbWriter.insert("NoteTable", null, cv);
        cv.clear();
    }

    //  读取数据
    public void readFromDB(List<Note> noteList,String className) {
        Cursor cursor = dbWriter.query("NoteTable", null, null, null, null, null, null);
        try {
                if (cursor.moveToFirst()) {
                    do{
                        String classname = cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CLASS_NAME));
                        if (className.equals(classname)) {
                            Note note = new Note();
                            note.setClassName(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CLASS_NAME)));
                            note.setId(cursor.getInt(cursor.getColumnIndex(NoteDBOpenHelper.ID)));
                            note.setTitle(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TITLE)));
                            note.setContent(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CONTENT)));
                            note.setTime(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TIME)));
                            noteList.add(note);
                        }
                    } while (cursor.moveToNext()) ;
                }
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  更新数据
    public void updateNote(String className,int noteID, String title, String content, String time) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDBOpenHelper.CLASS_NAME,className);
        cv.put(NoteDBOpenHelper.ID, noteID);
        cv.put(NoteDBOpenHelper.TITLE, title);
        cv.put(NoteDBOpenHelper.CONTENT, content);
        cv.put(NoteDBOpenHelper.TIME, time);
        dbWriter.update(NoteDBOpenHelper.TABLE_NAME, cv, "ID = ?", new String[]{noteID + ""});
    }

    //  删除数据
    public void deleteNote(int noteID) {
        dbWriter.delete(NoteDBOpenHelper.TABLE_NAME, "ID = ?", new String[]{noteID + ""});
    }

    // 根据id查询数据
    public Note readData(int noteID) {
        Cursor cursor = dbWriter.rawQuery("SELECT * FROM NoteTable WHERE ID = ?", new String[]{noteID + ""});
        cursor.moveToFirst();
        Note note = new Note();
        note.setClassName(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CLASS_NAME)));
        note.setId(cursor.getInt(cursor.getColumnIndex(NoteDBOpenHelper.ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CONTENT)));
        return note;
    }
}