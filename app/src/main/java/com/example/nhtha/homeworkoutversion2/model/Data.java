package com.example.nhtha.homeworkoutversion2.model;

import java.util.Date;

public class Data {
    private Date date;
    private Integer kCal;

    public Data(Date date, int kcal) {
        this.date = date;
        this.kCal = kcal;
    }


    public Integer getKCal() {
        return kCal;
    }

    public Date getDate() {
        return date;
    }

    public int getConvertDay(){
        return (int)(date.getTime()/(24*3600*1000)+1);
    }

}
