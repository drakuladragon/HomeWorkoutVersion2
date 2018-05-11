package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.callback.OnClick;
import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Movement;
import com.example.nhtha.homeworkoutversion2.presenter.ExercisePresenter;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.adapter.ExerciseAdapter;
import com.example.nhtha.homeworkoutversion2.view.dialog.ExercisePreviewDialog;

import java.util.List;

/**
 * Created by nhtha on 16-Jan-18.
 */

public class ExerciseFragment extends Fragment implements OnClick, View.OnClickListener {

    private RecyclerView rcvExercise;
    private ExerciseAdapter adapter;
    private ExercisePresenter exercisePresenter;

    private ImageView imgExImage;
    private TextView txtGo;

    private String exName;
    private String exCode;
    private int exImageID;
    private int currentItemPosition;
    private List<Movement> movements;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ExercisePreviewDialog exercisePreviewDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercisePresenter = new ExercisePresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtGo = getView().findViewById(R.id.txt_go);
        txtGo.setOnClickListener(this);
        init();
    }

    private void init() {

        if (exName == null || exCode == null) {
            return;
        }

        pullData();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (exName == null || exCode == null || exImageID == 0) {
            return;
        }

        pullData();

    }

    private void pullData() {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        ((StartActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ((StartActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((StartActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        collapsingToolbarLayout = getView().findViewById(R.id.ctl_title);
        collapsingToolbarLayout.setTitle(exName);

        imgExImage = getActivity().findViewById(R.id.img_title);
        imgExImage.setImageResource(exImageID);

        rcvExercise = getView().findViewById(R.id.rcv_all_ex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvExercise.setLayoutManager(linearLayoutManager);

      //  rcvExercise.setPadding(0,0,0,txtGo.getHeight());

        movements = exercisePresenter.getExerciseList(exCode);
        adapter = new ExerciseAdapter(getContext(),
                movements);
        adapter.setOnClick(this);

        rcvExercise.setAdapter(adapter);

        exercisePreviewDialog = new ExercisePreviewDialog(getContext());
        exercisePreviewDialog.setOnClick(this);
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    public void setExImageID(int exImageID) {
        this.exImageID = exImageID;
    }

    private void pullDialogData(int itemPosition){
        if (itemPosition == 0){
            exercisePreviewDialog.setImgBackEnable(false);
        } else {
            exercisePreviewDialog.setImgBackEnable(true);
        }
        if (itemPosition == adapter.getItemCount() - 1){
            exercisePreviewDialog.setImgNextEnable(false);
        } else {
            exercisePreviewDialog.setImgNextEnable(true);
        }

        Movement movement = adapter.getItem(itemPosition);

        String name = movement.getExName();
        String exIntro = "this is your help about this exercise, " +
                "if it difficult to understand please click the video icon";
        AnimationDrawable animationDrawable = movement.getMovementAnimation();
        String exSerial = String.valueOf(itemPosition + 1) + "/" + adapter.getItemCount();

        exercisePreviewDialog.setExName(name);
        exercisePreviewDialog.setExIntro(exIntro);
        exercisePreviewDialog.setExSerial(exSerial);
        exercisePreviewDialog.setImgExAnim(animationDrawable);
        animationDrawable.start();

    }

    @Override
    public void onItemCLick(int itemPosition) {
        currentItemPosition = itemPosition;
        pullDialogData(itemPosition);
        exercisePreviewDialog.show();
    }

    @Override
    public void onDialogNextClick() {
        pullDialogData(++currentItemPosition);
    }

    @Override
    public void onDialogBackClick() {
        pullDialogData(--currentItemPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_go:
                ((StartActivity) getActivity()).showDoingExerciseFragment(movements, adapter.getItemCount());
                break;

            default:
                break;
        }
    }


}
