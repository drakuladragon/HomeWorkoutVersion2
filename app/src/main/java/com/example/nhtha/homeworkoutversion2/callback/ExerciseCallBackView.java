package com.example.nhtha.homeworkoutversion2.callback;

import com.example.nhtha.homeworkoutversion2.model.Comment;
import com.example.nhtha.homeworkoutversion2.model.Movement;

import java.util.List;

public interface ExerciseCallBackView {
    void onLoadDataSuccess(List<Movement> movements);

}
