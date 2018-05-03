package com.example.nhtha.homeworkoutversion2.view.fragment.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.view.activity.LoginActivity;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Th016 on 03-Mar-18.
 */

public class RegisterFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View rootView;
    private Context context;

    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private Button btnCreateAccount;
    private TextView txtHaveAnAccount;

    private ProgressBar pgbRegister;
    private FirebaseAuth myFirebaseAuth;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_bone, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
        registerListeners();
    }

    private void initComponents() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        myFirebaseAuth = FirebaseAuth.getInstance();

        pgbRegister = rootView.findViewById(R.id.pgb_register);

        edtUserName = rootView.findViewById(R.id.edt_email);
        edtPassword = rootView.findViewById(R.id.edt_password);
        edtConfirmPassword = rootView.findViewById(R.id.edt_confirm_password);

        btnCreateAccount = rootView.findViewById(R.id.btn_creat_acc);
        txtHaveAnAccount = rootView.findViewById(R.id.txt_have_account);

    }

    private void registerListeners() {
        btnCreateAccount.setOnClickListener(this);
        txtHaveAnAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_creat_acc:
                registerAcc();
                break;
            case R.id.txt_have_account:
                ((LoginActivity) getActivity()).showLoginFragment();
                break;
            default:
                break;
        }
    }

    private void registerAcc() {
        pgbRegister.setVisibility(View.VISIBLE);
        final String email = edtUserName.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirmPass)) {

            if (pass.equals(confirmPass)) {

                myFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            saveData();

                        } else {

                            String error = task.getException().getMessage();
                            Log.d("registeracc", "onComplete: " + error);
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        }

                        pgbRegister.setVisibility(View.INVISIBLE);

                    }

                    private void saveData() {
                        Uri uri = getUriToDrawable(getContext(), R.drawable.img_user);
                        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        StorageReference storage = FirebaseStorage.getInstance().getReference();
                        storage.child("profiles_image").child(userID + ".jpg")
                                .putFile(uri)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {

                                            UserDto userDto = new UserDto();
                                            userDto.setName(email);
                                            userDto.setAvatar(task.getResult().getDownloadUrl().toString());
                                            userDto.setKcal(0);
                                            userDto.setWorkout(0);

                                            databaseReference
                                                    .child("users")
                                                    .child(userID)
                                                    .setValue(userDto);

                                            sendToStart();

                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                });

            } else {
                Toast.makeText(getContext(), "Wrong confirm password", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public Uri getUriToDrawable(@NonNull Context context,
                                @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent intent = new Intent(getContext(), StartActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
