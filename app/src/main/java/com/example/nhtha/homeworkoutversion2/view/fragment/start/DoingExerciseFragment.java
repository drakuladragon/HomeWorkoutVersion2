package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Movement;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.tooltip.Tooltip;

import java.util.List;

/**
 * Created by nhtha on 22-Jan-18.
 */

@SuppressLint("ValidFragment")
public class DoingExerciseFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DoingExerciseFragment";
    private RelativeLayout rlDoingExercise;
    private ImageView imgIntro;
    private TextView txtExName;
    private TextView txtExTurn;
    private ImageView imgDone;
    private ImageView imgPre;
    private ImageView imgNext;
    private ImageView imgHelp;
    private ImageView imgBack;

    private RelativeLayout rlShortBreak;
    private ProgressBar pgbTimeCoolDown;
    private TextView txtTimeCoolDown;
    private TextView txtSkip;

    private List<Movement> movementList;
    private int position;
    private int itemCount;

    private TimeAsyncTask timeAsyncTask;
    private Tooltip tooltip;

    @SuppressLint("ValidFragment")
    public DoingExerciseFragment(List<Movement> movementList) {
        this.movementList = movementList;
        itemCount = movementList.size();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doing_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        rlDoingExercise = getView().findViewById(R.id.rl_doing_exercise);
        imgIntro = getView().findViewById(R.id.img_intro);
        txtExName = getView().findViewById(R.id.txt_ex_name);
        txtExTurn = getView().findViewById(R.id.txt_ex_turn);
        imgDone = getView().findViewById(R.id.img_done);
        imgPre = getView().findViewById(R.id.img_pre);
        imgNext = getView().findViewById(R.id.img_next);
        imgHelp = getView().findViewById(R.id.img_help);
        imgBack = getView().findViewById(R.id.img_back);

        rlShortBreak = getView().findViewById(R.id.rl_short_break);
        pgbTimeCoolDown = getView().findViewById(R.id.pgb_cooldown);
        txtTimeCoolDown = getView().findViewById(R.id.txt_time_left);
        txtSkip = getView().findViewById(R.id.txt_skip);
        tooltip = new Tooltip.Builder(imgHelp)
                .setText("this is your help about this exercise, " +
                        "if it difficult to understand please click the video icon")
                .setCancelable(true)
                .setDismissOnClick(true)
                .setArrowHeight(15.00f)
                .setArrowWidth(10.00f)
                .setCornerRadius(10.00f)
                .setPadding(8.0f)
                .setTextColor(Color.WHITE)
                .setTextSize(16.00f)
                .build();

        imgPre.setVisibility(View.GONE);
        imgPre.setOnClickListener(this);
        imgDone.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
        imgHelp.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        showExercise();
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
        itemCount = movementList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_done:
                if (position == itemCount - 1){
                    setNextInvisible();
                    //TODO start fireworf fragment

                    ((StartActivity) getActivity()).showCompleteExFragment(itemCount);
                }

                if (position > itemCount - 2){
                    return;
                }
                position++;

                setPreVisible();
                setRlShortBreakVisible();

                showExercise();

                timeAsyncTask = new TimeAsyncTask();
                timeAsyncTask.execute();
                break;

            case R.id.img_next:
                if (position == itemCount - 1){
                    setNextInvisible();
                    //TODO start fireworf fragment

                    ((StartActivity) getActivity()).showCompleteExFragment(itemCount);
                }

                if (position > itemCount - 2){
                    return;
                }
                position++;

                setPreVisible();
                setRlShortBreakVisible();

                showExercise();

                timeAsyncTask = new TimeAsyncTask();
                timeAsyncTask.execute();
                break;

            case R.id.img_pre:
                if (position < 1 || position > itemCount - 2){
                    return;
                }
                position--;

                showExercise();
                break;

            case R.id.txt_skip:
                setRlDoingExerciseVisible();
                timeAsyncTask.cancel();
                break;

            case R.id.img_help:
                tooltip.show();
                break;

            case R.id.img_back:
                ((StartActivity)getActivity()).getSupportFragmentManager().popBackStack();
                break;

            default:
                break;
        }
    }

    private void showExercise() {
        if (movementList == null) {
            return;
        }

        Movement movement = movementList.get(position);

        String exName = movement.getExName();
        String exTurn = movement.getExTurn();
        AnimationDrawable animationDrawable = movement.getMovementAnimation();

        txtExName.setText(exName);
        txtExTurn.setText(exTurn);

        imgIntro.clearAnimation();
        imgIntro.setBackground(animationDrawable);
        animationDrawable.start();
        animationDrawable.run();

        Log.d(TAG, "showExercise: " + animationDrawable.getNumberOfFrames());
        Log.d(TAG, "showExercise: " + animationDrawable.isRunning());
    }

    private void setNextInvisible(){
        if (position > itemCount - 1){
            imgNext.setVisibility(View.GONE);
        }
    }

    private void setPreVisible() {
        if (position == 0) {
            return;
        }
        imgPre.setVisibility(View.VISIBLE);
    }

    private void setRlDoingExerciseVisible() {
        rlDoingExercise.setVisibility(View.VISIBLE);
        rlShortBreak.setVisibility(View.GONE);
    }

    private void setRlShortBreakVisible() {
        rlDoingExercise.setVisibility(View.GONE);
        rlShortBreak.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.d(TAG, "onHiddenChanged:... " );
            position = 0;
            showExercise();
        }
    }

    class TimeAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int value;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            value = 30;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (value > 0) {
                value--;
                publishProgress(value);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            txtTimeCoolDown.setText(values[0] + "");
            pgbTimeCoolDown.setProgress(values[0]);
            if (values[0] <= 0) {
                setRlDoingExerciseVisible();
                cancel(false);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void cancel(){
            value = 0;
            publishProgress(value);
        }
    }
}
