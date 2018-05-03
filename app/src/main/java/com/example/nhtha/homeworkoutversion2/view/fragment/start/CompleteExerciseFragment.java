package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
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

@SuppressLint("ValidFragment")
public class CompleteExerciseFragment extends Fragment {

    private TextView txtFireKcal;
    private ImageView imgComplete;
    private int kcal;

    FirebaseUser user;
    DatabaseReference userDatabaseReference;

    @SuppressLint("ValidFragment")
    public CompleteExerciseFragment(int kcal) {
        this.kcal = kcal;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fireworks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        txtFireKcal = getView().findViewById(R.id.txt_fire_kcal);
        imgComplete = getView().findViewById(R.id.img_complete);

        pullData();
        imgComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
                goToStartFragment();
            }
        });
    }

    private void sendData() {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);

                int calo = userDto.getKcal() + kcal;
                int workout = userDto.getWorkout() + 1;

                userDto.setKcal(calo);
                userDto.setWorkout(workout);

                userDatabaseReference.setValue(userDto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void pullData() {
        txtFireKcal.setText("You already fire " + kcal + " Kcal");
    }

    private void goToStartFragment() {
        ((StartActivity) getActivity()).showStartFragment();
    }
}
