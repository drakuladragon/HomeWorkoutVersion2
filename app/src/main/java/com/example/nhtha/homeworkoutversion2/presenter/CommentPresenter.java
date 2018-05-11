package com.example.nhtha.homeworkoutversion2.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.callback.CommentCallBackView;
import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.model.Comment;
import com.example.nhtha.homeworkoutversion2.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference commentReference;
    private DatabaseReference userDatabaseReference;
    private List<CommentDto> commentDtoList;
    private ProgressDialog progressDialog;
    private CommentCallBackView callBackView;
    private List<Comment> comments;
    private List<User> userList;
    private FirebaseUser user;


    public CommentPresenter(Context context) {
        this.context = context;
        commentReference = FirebaseDatabase.getInstance().getReference().child("comment");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        commentDtoList = new ArrayList<>();
        comments = new ArrayList<>();
        userList = new ArrayList<>();
    }

    public void addComment(final String commentDes, final String authorID, final String postID) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        userDatabaseReference.child(authorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommentDto commentDto = new CommentDto();

                commentDto.setAuthorID(authorID);
                commentDto.setPostID(postID);
                commentDto.setCommentDes(commentDes);

                DatabaseReference addCommentReference = commentReference.push();
                addCommentReference.setValue(commentDto);

                progressDialog.dismiss();

                final Comment comment = new Comment();
                comment.setAuthorID(authorID);
                comment.setPostID(postID);
                comment.setCommentDes(commentDes);
                DatabaseReference currUser = userDatabaseReference.child(user.getUid());
                currUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDto userDto = dataSnapshot.getValue(UserDto.class);
                        comment.setAuthorAvatar(userDto.getAvatar());
                        comment.setAuthorName(userDto.getName());
                        callBackView.onPushCommentSuccess(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT);
            }
        });


    }

    public void loadCommentList(){
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserDto userDto = snapshot.getValue(UserDto.class);
                    User user = new User();
                    user.setId(snapshot.getKey());
                    user.setName(userDto.getName());
                    user.setAvatar(userDto.getAvatar());
                    userList.add(user);
                    loadCommentDtos();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadCommentDtos(){
        commentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentDto commentDto = snapshot.getValue(CommentDto.class);
                    Comment comment = new Comment();
                    comment.setAuthorID(commentDto.getAuthorID());
                    comment.setCommentID(snapshot.getKey());
                    comment.setCommentDes(commentDto.getCommentDes());
                    comment.setPostID(commentDto.getPostID());
                    for (User user : userList){
                        if (comment.getAuthorID().equals(user.getId())){
                            comment.setAuthorAvatar(user.getAvatar());
                            comment.setAuthorName(user.getName());
                            break;
                        }
                    }
                    comments.add(comment);
                    callBackView.onLoadSuccess(comments);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setCallBackView(CommentCallBackView callBackView) {
        this.callBackView = callBackView;
    }
}
