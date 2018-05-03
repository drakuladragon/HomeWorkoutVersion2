package com.example.nhtha.homeworkoutversion2.view.dialog;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nhtha.homeworkoutversion2.R;


/**
 * Created by nhtha on 27-Feb-18.
 */

public class RemiderDialog extends android.app.Dialog {

    private TimePicker timePicker;
    private TextView txtNext;
    private TextView txtCancelOne;

    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaturday;
    private CheckBox cbSunday;
    private TextView txtOk;
    private TextView txtCancelTwo;

    private RelativeLayout rlTimePicker;
    private RelativeLayout rlDateRepeat;

    private OnRemiderDialogClick onClick;

    public RemiderDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.dialog_time_picker);
        init();
    }

    private void init() {
        timePicker = findViewById(R.id.timePicker);
        txtNext = findViewById(R.id.txt_next);
        txtCancelOne = findViewById(R.id.txt_cancel_1);

        cbMonday = findViewById(R.id.cb_monday);
        cbTuesday = findViewById(R.id.cb_tuesday);
        cbWednesday = findViewById(R.id.cb_wednesday);
        cbThursday = findViewById(R.id.cb_thursday);
        cbFriday = findViewById(R.id.cb_friday);
        cbSaturday = findViewById(R.id.cb_saturday);
        cbSunday = findViewById(R.id.cb_sunday);
        txtOk = findViewById(R.id.txt_ok);
        txtCancelTwo = findViewById(R.id.txt_cancel_2);

        rlTimePicker = findViewById(R.id.rl_timepicker);
        rlDateRepeat = findViewById(R.id.rl_repeat_day);

        txtNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                onClick.onButtonNextClick(timePicker.getHour(),timePicker.getMinute());
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onButtonOkClick(new boolean[]{cbMonday.isChecked()
                        ,cbTuesday.isChecked()
                        ,cbWednesday.isChecked()
                        ,cbThursday.isChecked()
                        ,cbFriday.isChecked()
                        ,cbSaturday.isChecked()
                        ,cbSunday.isChecked()});
            }
        });

        txtCancelOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onButtonCancelOneClick();
            }
        });

        txtCancelTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onButtonCancelTwoClick();
            }
        });
    }

    public void showTimePicker(int visible){
        rlTimePicker.setVisibility(visible);
    }

    public void showDateRepeat(int visible){
        rlDateRepeat.setVisibility(visible);
    }

    public void setOnClick(OnRemiderDialogClick onClick) {
        this.onClick = onClick;
    }

    public interface OnRemiderDialogClick {
        void onButtonNextClick(int hour, int minute);

        void onButtonOkClick(boolean[] date);

        void onButtonCancelOneClick();

        void onButtonCancelTwoClick();
    }

}
