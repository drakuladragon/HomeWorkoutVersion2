package com.example.nhtha.homeworkoutversion2.presenter;

import android.view.View;
import android.widget.ProgressBar;

import com.example.nhtha.homeworkoutversion2.callback.PostCallBackVIew;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.model.Post;
import com.example.nhtha.homeworkoutversion2.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostPresenter {

    private PostCallBackVIew postListView;
    private DatabaseReference postReference;
    private DatabaseReference userReference;
    private List<Post> postList;
    private List<PostDto> postDtoList;
    private List<User> userList;
    private ProgressBar progressBar;

    public PostPresenter(ProgressBar progressBar) {
        postReference = FirebaseDatabase.getInstance().getReference().child("post");
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        userList = new ArrayList<>();
        postList = new ArrayList<>();
        postDtoList = new ArrayList<>();
        this.progressBar = progressBar;
        this.progressBar.setVisibility(View.VISIBLE);
        loadPostList();
    }

    public void loadPostList(){
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserDto userDto = snapshot.getValue(UserDto.class);
                    User user = new User();
                    user.setId(snapshot.getKey());
                    user.setName(userDto.getName());
                    user.setAvatar(userDto.getAvatar());
                    userList.add(user);
                    loadPostDtos();
                    stopLoading();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadPostDtos(){
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostDto postDto = snapshot.getValue(PostDto.class);
                    Post post = new Post();
                    post.setTitle(postDto.getTitle());
                    post.setContent(postDto.getContent());
                    post.setImageURL(postDto.getImageURL());
                    post.setAuthorId(postDto.getAuthorId());
                    post.setPostID(snapshot.getKey());
                    for (User user : userList){
                        if (post.getAuthorId().equals(user.getId())){
                            post.setUserAvatar(user.getAvatar());
                            post.setUserName(user.getName());
                            break;
                        }
                    }
                    postList.add(post);
                    postListView.onLoadSucces(postList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setPostListView(PostCallBackVIew postListView) {
        this.postListView = postListView;
    }

    private void stopLoading(){
        progressBar.setVisibility(View.GONE);
    }
}
