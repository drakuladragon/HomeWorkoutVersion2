package com.example.nhtha.homeworkoutversion2.presenter;

import android.util.Log;

import com.example.nhtha.homeworkoutversion2.callback.UserCallBack;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
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
