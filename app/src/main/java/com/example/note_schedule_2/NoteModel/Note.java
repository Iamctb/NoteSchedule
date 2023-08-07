package com.example.note_schedule_2.NoteModel;

import java.io.Serializable;

public class Note implements Serializable {
    private String className;
    private int id;
    private String title;
    private String content;
    private String time;

    public String getClassName(){
        return className;
    }

    public void setClassName(String className){
        this.className=className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
