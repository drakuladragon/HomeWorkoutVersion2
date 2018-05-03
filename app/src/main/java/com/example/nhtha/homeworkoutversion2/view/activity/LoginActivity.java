package com.example.nhtha.homeworkoutversion2.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.nhtha.homeworkoutversion2.view.fragment.login.LoginFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.login.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        showLoginFragment();
    }

    public void showLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content,loginFragment)
                .commit();
    }

    public void showRegisterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content,registerFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
