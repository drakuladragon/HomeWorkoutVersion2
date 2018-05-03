package com.example.nhtha.homeworkoutversion2.view.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.view.activity.LoginActivity;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Th016 on 03-Mar-18.
 */

public class LoginFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View rootView;

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLoginAccount;
    private TextView txtRegister;

    private String name;
    private String id;

    private FirebaseAuth myFirebaseAuth;
    private ProgressBar pgbLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_bone, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }


    private void initComponents() {

        myFirebaseAuth = FirebaseAuth.getInstance();

        pgbLogin = rootView.findViewById(R.id.pgb_login);
        edtUserName = rootView.findViewById(R.id.edt_email);
        edtPassword = rootView.findViewById(R.id.edt_password);
        btnLoginAccount = rootView.findViewById(R.id.btn_login);
        txtRegister = rootView.findViewById(R.id.txt_need_account);

        btnLoginAccount.setOnClickListener(this);
        txtRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.txt_need_account:
                ((LoginActivity) getActivity()).showRegisterFragment();
                break;
        }
    }

    private void login() {


        String email = edtUserName.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

            pgbLogin.setVisibility(View.VISIBLE);

            myFirebaseAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                sendToStart();

                            } else {
                                Toast.makeText(getContext(),"Login Fail",Toast.LENGTH_SHORT).show();
                            }

                            pgbLogin.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = myFirebaseAuth.getCurrentUser();

        if (user != null){

           sendToStart();

        }

    }

    private void sendToStart() {
        Intent intent = new Intent(getContext(), StartActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
