package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.presenter.CommentPresenter;
import com.example.nhtha.homeworkoutversion2.presenter.UserPresenter;
import com.example.nhtha.homeworkoutversion2.view.adapter.CommentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhtha on 09-Mar-18.
 */

@SuppressLint("ValidFragment")
public class CommentFragment extends Fragment implements View.OnClickListener {

    private EditText edtComment;
    private ImageView imgSend;

    private CommentAdapter commentAdapter;
    private RecyclerView rcvComment;
    private List<CommentDto> commentDtoList;
    private CommentPresenter commentPresenter;

    private DatabaseReference commentReference;

    private String postID;

    @SuppressLint("ValidFragment")
    public CommentFragment(String postID) {
        this.postID = postID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentDtoList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        commentReference = FirebaseDatabase.getInstance().getReference().child("comment");

        commentPresenter = new CommentPresenter(getContext());

        rcvComment = getView().findViewById(R.id.rcv_comment_list);
        edtComment = getView().findViewById(R.id.edt_comment);
        imgSend = getView().findViewById(R.id.img_send);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvComment.setLayoutManager(layoutManager);


        pullData();

        commentAdapter = new CommentAdapter(getContext(), commentDtoList);
        rcvComment.setAdapter(commentAdapter);

        imgSend.setOnClickListener(this);
        edtComment.setSelected(true);


    }

    private void pullData() {

        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                commentDtoList.clear();

                for (DataSnapshot commentSnapShot : dataSnapshot.getChildren()) {
                    CommentDto commentDto = commentSnapShot.getValue(CommentDto.class);
                    if (commentDto.getPostID().trim().equals(postID)) {
                        commentDtoList.add(commentDto);
                    }
                }

                commentAdapter.notifiDataChanged(commentDtoList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_send:
                sendComment();
                break;

            default:
                break;
        }
    }

    private void sendComment() {
        String commentDes = edtComment.getText().toString().trim();
        if (!TextUtils.isEmpty(commentDes)) {

            String authorID = UserPresenter.getUserID();

            commentPresenter.addComment(commentDes, authorID, postID);
            commentAdapter.notifiDataChanged(commentDtoList);

        }
        edtComment.setText("");
        edtComment.setSelected(false);
    }

}
