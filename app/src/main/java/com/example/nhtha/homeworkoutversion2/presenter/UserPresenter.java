package com.example.nhtha.homeworkoutversion2.presenter;

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

    public static String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        return userID;
    }
}
