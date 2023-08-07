package com.example.note_schedule_2.NoteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    SimpleDateFormat simpleDateFormat= new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");
    Date currentDate=new Date(System.currentTimeMillis());

    public String getCurrentTime(){
        String time=simpleDateFormat.format(currentDate);
        return time;
    }
}
