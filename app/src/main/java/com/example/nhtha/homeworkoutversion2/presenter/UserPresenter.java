package com.example.nhtha.homeworkoutversion2.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.callback.UserCallBack;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nhtha on 09-Mar-18.
 */

public class UserPresenter {

    private DatabaseReference databaseReference;
    private UserCallBack callBack;

    public UserPresenter() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        getUserData();
    }

    public static String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        return userID;
    }

    public static void changePassword(final Context context, String curPassword, final String newPassword){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), curPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context,"Passwor dUpdated",Toast.LENGTH_SHORT).show();
                                        Log.d("", "Password updated");
                                    } else {
                                        Toast.makeText(context,"Password Update Fail",Toast.LENGTH_SHORT).show();
                                        Log.d("", "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d("Error", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getUserData(){
        databaseReference.child(getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);
                callBack.onLoadSuccess(userDto.getName(),userDto.getAvatar());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setCallBack(UserCallBack callBack) {
        this.callBack = callBack;
    }
}
