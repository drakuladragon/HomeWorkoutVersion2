package com.example.nhtha.homeworkoutversion2.model;

/**
 * Created by nhtha on 28-Feb-18.
 */

public class Reminder {
    public static final int CHECKED = 1;
    public static final int NOT_CHECKED = 0;

    private String time;
    private String date;
    private int checked;

    public Reminder(String time, String date, int checked) {
        this.time = time;
        this.date = date;
        this.checked = checked;
    }

    public int getChecked() {

        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
