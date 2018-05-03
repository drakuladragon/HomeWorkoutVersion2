package com.example.nhtha.homeworkoutversion2.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.OnClick;
import com.example.nhtha.homeworkoutversion2.R;

/**
 * Created by nhtha on 24-Feb-18.
 */

public class ExercisePreviewDialog extends Dialog {

    private TextView txtExName;
    private ImageView imgExAnim;
    private TextView txtExIntro;
    private TextView txtExSerial;
    private ImageView imgNext;
    private ImageView imgBack;

    private OnClick onClick;

    public ExercisePreviewDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_preview_exercise);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        init();
    }

    private void init() {
        txtExName=findViewById(R.id.txt_ex_name);
        imgExAnim = findViewById(R.id.img_preview);
        txtExIntro = findViewById(R.id.txt_ex_intro);
        txtExSerial = findViewById(R.id.txt_ex_serial);
        imgBack = findViewById(R.id.img_back);
        imgNext = findViewById(R.id.img_next);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onDialogBackClick();
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onDialogNextClick();
            }
        });

    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setExName(String exName){
        if (exName.isEmpty()){
            return;
        }
        txtExName.setText(exName);
    }

    public void setExIntro(String exIntro){
        if (exIntro.isEmpty()){
            return;
        }
        txtExIntro.setText(exIntro);
    }

    public void setExSerial(String exSerial){
        if (exSerial.isEmpty()){
            return;
        }
        txtExSerial.setText(exSerial);
    }

    public void setImgExAnim(AnimationDrawable anim){
        if (anim == null){
            return;
        }
        imgExAnim.clearAnimation();
        imgExAnim.setBackground(anim);
        anim.start();
    }

    public void setImgNextEnable(boolean enable){
        imgNext.setEnabled(enable);
    }

    public void setImgBackEnable(boolean enable) {
        imgBack.setEnabled(enable);
    }
}
