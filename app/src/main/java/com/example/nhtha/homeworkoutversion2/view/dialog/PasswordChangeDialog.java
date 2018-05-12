package com.example.nhtha.homeworkoutversion2.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.presenter.UserPresenter;

public class PasswordChangeDialog extends Dialog{

    private EditText edtCurrentPassword;
    private EditText edtNewPassWord;
    private EditText edtConfirmPassword;
    private TextView tvCancel;
    private TextView tvChange;

    @SuppressLint("ResourceAsColor")
    public PasswordChangeDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        setContentView(R.layout.dialog_password_change);
        init();
    }

    private void init() {
        edtCurrentPassword = findViewById(R.id.edt_current_pwd);
        edtNewPassWord = findViewById(R.id.edt_new_pwd);
        edtConfirmPassword = findViewById(R.id.edt_confirm_new_pwd);
        tvCancel = findViewById(R.id.tv_cancel);
        tvChange = findViewById(R.id.tv_change);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEditText();
                dismiss();
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changePassWord();
            }
        });
    }

    private void changePassWord() {
        String newPass = edtNewPassWord.getText().toString();
        String confirmPass = edtConfirmPassword.getText().toString();
        String currentPass = edtCurrentPassword.getText().toString();
        if (newPass.isEmpty() || currentPass.isEmpty()){
            Toast.makeText(getContext(),"Password can be null",Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.equals(confirmPass) && !TextUtils.isDigitsOnly(currentPass)){
            UserPresenter.changePassword(getContext(),currentPass,newPass);
        }
    }

    private void clearEditText(){
        edtNewPassWord.setText("");
        edtCurrentPassword.setText("");
        edtConfirmPassword.setText("");
    }

}
