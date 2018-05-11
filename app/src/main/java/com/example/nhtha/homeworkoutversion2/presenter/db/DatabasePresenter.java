package com.example.nhtha.homeworkoutversion2.presenter.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.example.nhtha.homeworkoutversion2.model.Movement;
import com.example.nhtha.homeworkoutversion2.model.Reminder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhtha on 16-Jan-18.
 */

public class DatabasePresenter {
    public static final String COLLUM_STT = "STT";
    public static final String COLLUM_MOVEMENT_CODE = "MovementCode";
    public static final String COLLUM_MOVEMENT_NAME = "MovementName";
    public static final String COLLUM_MOVEMENT_TURN = "MovementTurn";


    private String databaseName;
    private String databasePath;

    protected Context context;
    SQLiteDatabase sqLiteDatabase;

    public DatabasePresenter(Context context) {
        this.context = context;
        databaseName = "exercises.db";
        databasePath = context.getFilesDir().getPath() + File.separator + databaseName;
        copyDatabaseFromAssets(context);
    }

    private void copyDatabaseFromAssets(Context context) {
        File file = new File(databasePath);

        if (file.exists()) {
            return;
        }

        try {
            //get inputStream
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(databaseName, AssetManager.ACCESS_STREAMING);

            //create outputStream
            FileOutputStream outputStream = new FileOutputStream(file);

            byte buff[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, length);
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDatabase() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
        }

    }

    private void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public List<String> getExID(String tableName) {
        openDatabase();
        String sqlQuerry = "select * from " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuerry, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        List<String> strings = new ArrayList<>();
        cursor.moveToFirst();
        //trong khi chưa phải bản ghi cuối
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(COLLUM_STT));

            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return strings;
    }

    public List<String> getExName(String tableName) {
        String sqlQuerry = "select * from " + tableName;
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuerry, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        List<String> strings = new ArrayList<>();
        cursor.moveToFirst();
        //trong khi chưa phải bản ghi cuối
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(COLLUM_MOVEMENT_NAME));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return strings;
    }

    public List<Movement> getExercise(String tableName) {
        String sqlQuerry = "select * from " + tableName;
        openDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuerry, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        List<Movement> movements = new ArrayList<>();
        cursor.moveToFirst();
        //trong khi chưa phải bản ghi cuối
        while (!cursor.isAfterLast()) {
            String code = cursor.getString(cursor.getColumnIndex(COLLUM_MOVEMENT_CODE));
            String name = cursor.getString(cursor.getColumnIndex(COLLUM_MOVEMENT_NAME));
            String turn = cursor.getString(cursor.getColumnIndex(COLLUM_MOVEMENT_TURN));
            Movement movement = new Movement(name, turn, code);
            movements.add(movement);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

        return movements;
    }

    public List<Reminder> getRemiderList() {
        String sqlQuerry = "select * from reminder";

        openDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuerry, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        List<Reminder> reminders = new ArrayList<>();
        cursor.moveToFirst();

        //trong khi chưa phải bản ghi cuối
        while (!cursor.isAfterLast()) {
            String time = cursor.getString(cursor.getColumnIndex("Time"));
            String date = cursor.getString(cursor.getColumnIndex("DateChecked"));
            int isChecked = cursor.getInt(cursor.getColumnIndex("IsChecked"));
            Reminder reminder = new Reminder(time, date, isChecked);
            reminders.add(reminder);
            cursor.moveToNext();
        }

        cursor.close();

        closeDatabase();

        return reminders;
    }

    public void addReminder(int id, Reminder reminder) {
        String sqlQuerry = "insert into reminder values (" + id + "," +
                reminder.getTime() + "," +
                reminder.getChecked() + "," +
                reminder.getDate() + ")";

        openDatabase();
        try {
            sqLiteDatabase.compileStatement(sqlQuerry);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeDatabase();
    }


}
