package com.example.nhtha.homeworkoutversion2.callback;

import com.example.nhtha.homeworkoutversion2.model.Comment;

import java.util.List;

public interface CommentCallBackView {
    void onLoadSuccess(List<Comment> commentList);
    void onPushCommentSuccess(Comment comment);
}
