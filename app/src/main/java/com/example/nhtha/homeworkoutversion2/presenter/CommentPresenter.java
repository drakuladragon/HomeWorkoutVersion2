package com.example.nhtha.homeworkoutversion2.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
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

public class CommentPresenter {

    private Context context;
    private DatabaseReference databaseReference;
    private DatabaseReference userDatabaseReference;
    private List<CommentDto> commentDtoList;
    private ProgressDialog progressDialog;

    public CommentPresenter(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("comment");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        commentDtoList = new ArrayList<>();
    }

    public void addComment(final String commentDes, final String authorID, final String postID) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        userDatabaseReference.child(authorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDto userDto = dataSnapshot.getValue(UserDto.class);

                String authorName = userDto.getName();
                String authorAvatar = userDto.getAvatar();

                CommentDto commentDto = new CommentDto();

                commentDto.setAuthorAvatar(authorAvatar);
                commentDto.setAuthorID(authorID);
                commentDto.setPostID(postID);
                commentDto.setCommentDes(commentDes);
                commentDto.setAuthorName(authorName);

                DatabaseReference addCommentReference = databaseReference.push();
                addCommentReference.setValue(commentDto);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT);
            }
        });


    }

    public List<CommentDto> getCommentList(final String postID) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot commentSnapShot : dataSnapshot.getChildren()) {
                    CommentDto commentDto = commentSnapShot.getValue(CommentDto.class);
                    if (commentDto.getPostID().trim().equals(postID)) {
                        Log.d("COMMENTDTOID", "onDataChange: " + commentDto.getPostID());
                        Log.d("POSTID", "onDataChange: " + postID);
                        commentDtoList.add(commentDto);
                        Log.d("LISTSIZE", "onDataChange: " + commentDtoList.size());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("SIZE", "getCommentList: " + commentDtoList.size());
        return commentDtoList;
    }

}
