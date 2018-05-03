package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.presenter.ExercisePresenter;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartFragment extends Fragment implements View.OnClickListener {

    private ImageView imgChestBeginer;
    private ImageView imgChestIntermediate;
    private ImageView imgChestAdvance;
    private ImageView imgAbsBeginner;
    private ImageView imgAbsIntermediate;
    private ImageView imgAbsAdvance;
    private ImageView imgShoulderBeginner;
    private ImageView imgShoulderIntermediate;
    private ImageView imgShoulderAdvance;
    private ImageView imgLegBeginner;
    private ImageView imgLegIntermediate;
    private ImageView imgLegAdvance;

    private ImageView imgMenu;

    private TextView txtWorkout;
    private TextView txtKcal;

    private View rootView;

    FirebaseUser user;
    DatabaseReference userDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        registerListener();
    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        txtWorkout = rootView.findViewById(R.id.txt_work_times);
        txtKcal = rootView.findViewById(R.id.txt_kcal_numeral);

        imgChestBeginer = rootView.findViewById(R.id.img_chest_beginer);
        imgChestIntermediate = rootView.findViewById(R.id.img_chest_intermediate);
        imgChestAdvance = rootView.findViewById(R.id.img_chest_advance);

        imgAbsBeginner = rootView.findViewById(R.id.img_abs_beginner);
        imgAbsIntermediate = rootView.findViewById(R.id.img_abs_intermediate);
        imgAbsAdvance = rootView.findViewById(R.id.img_abs_advance);

        imgShoulderBeginner = rootView.findViewById(R.id.img_sb_beginner);
        imgShoulderIntermediate = rootView.findViewById(R.id.img_sb_intermediate);
        imgShoulderAdvance = rootView.findViewById(R.id.img_sb_advance);

        imgLegBeginner = rootView.findViewById(R.id.img_leg_beginner);
        imgLegIntermediate = rootView.findViewById(R.id.img_leg_intermediate);
        imgLegAdvance = rootView.findViewById(R.id.img_leg_advance);

        imgMenu = getActivity().findViewById(R.id.img_menu);

        imgChestBeginer.setOnClickListener(this);
        imgChestIntermediate.setOnClickListener(this);
        imgChestAdvance.setOnClickListener(this);
        imgMenu.setOnClickListener(this);
    }

    private void registerListener() {
        imgChestBeginer.setOnClickListener(this);
        imgChestIntermediate.setOnClickListener(this);
        imgChestAdvance.setOnClickListener(this);

        imgAbsBeginner.setOnClickListener(this);
        imgAbsIntermediate.setOnClickListener(this);
        imgAbsAdvance.setOnClickListener(this);

        imgShoulderBeginner.setOnClickListener(this);
        imgShoulderIntermediate.setOnClickListener(this);
        imgShoulderAdvance.setOnClickListener(this);

        imgLegBeginner.setOnClickListener(this);
        imgLegIntermediate.setOnClickListener(this);
        imgLegAdvance.setOnClickListener(this);

        imgMenu.setOnClickListener(this);

        setData();
    }

    private void setData() {
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);
                txtKcal.setText(String.valueOf(userDto.getKcal()) + "");
                txtWorkout.setText(String.valueOf(userDto.getWorkout()) + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_chest_beginer:
                showExercise("CHEST_BEGINNER",
                        ExercisePresenter.CHEST_BEGINER,
                        R.drawable.cover_chest_1);
                break;

            case R.id.img_chest_intermediate:
                showExercise("CHEST INTERMEDIATE",
                        ExercisePresenter.CHEST_INTERMEDIATE,
                        R.drawable.cover_chest_2);
                break;

            case R.id.img_chest_advance:
                showExercise("CHEST ADVANCE",
                        ExercisePresenter.CHEST_ADVANCE,
                        R.drawable.cover_chest_3);
                break;

            case R.id.img_abs_beginner:
                showExercise("ABS_BEGINNER",
                        ExercisePresenter.ABS_BEGINNER,
                        R.drawable.cover_abs_1);
                break;

            case R.id.img_abs_intermediate:
                showExercise("ABS_INTERMEDIATE",
                        ExercisePresenter.ABS_INTERMEDIATE,
                        R.drawable.cover_abs_2);
                break;

            case R.id.img_abs_advance:
                showExercise("ABS_ADVANCE",
                        ExercisePresenter.ABS_ADVANCE,
                        R.drawable.cover_abs_3);
                break;

            case R.id.img_sb_beginner:
                showExercise("SHOUDER & BACK BEGINNER",
                        ExercisePresenter.SB_BEGINNER,
                        R.drawable.cover_shoulder_1);
                break;

            case R.id.img_sb_intermediate:
                showExercise("SHOUDER & BACK INTERMEDIATE",
                        ExercisePresenter.SB_INTERMEDIATE,
                        R.drawable.cover_shoulder_2);
                break;

            case R.id.img_sb_advance:
                showExercise("SHOUDER & BACK ADVANCE",
                        ExercisePresenter.SB_ADVANCE,
                        R.drawable.cover_shoulder_3);
                break;

            case R.id.img_leg_beginner:
                showExercise("LEG BEGINNER",
                        ExercisePresenter.LEG_BEGINNER,
                        R.drawable.cover_leg_1);
                break;

            case R.id.img_leg_intermediate:
                showExercise("LEG INTERMEDIATE",
                        ExercisePresenter.LEG_INTERMEDIATE,
                        R.drawable.cover_leg_2);
                break;

            case R.id.img_leg_advance:
                showExercise("LEG ADVANCE",
                        ExercisePresenter.LEG_ADVANCE,
                        R.drawable.cover_leg_3);
                break;

            case R.id.img_menu:
                ((StartActivity) getActivity()).openDrawer();
                break;

            default:
                break;
        }
    }

    private void showExercise(String exName, String exCode, int toolBarImageId) {
        ((StartActivity) getActivity()).showExerciseFragment(exName,
                exCode, toolBarImageId);
    }

}
