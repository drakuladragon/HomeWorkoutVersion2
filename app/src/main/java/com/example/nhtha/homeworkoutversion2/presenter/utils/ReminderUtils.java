package com.example.nhtha.homeworkoutversion2.presenter.utils;

/**
 * Created by nhtha on 04-Mar-18.
 */

public class ReminderUtils {

    public static boolean getChecked(int checked){
        if (checked != 0){
            return true;
        }
        return false;
    }

    public static int getHour(String time){
        int hour = Integer.parseInt(time.substring(0,time.indexOf(":")));
        return  hour;
    }

    public static int getMinute(String time){
        int min = Integer.parseInt(time.substring(time.indexOf(":") + 1,time.length()));
        return min;
    }

}
