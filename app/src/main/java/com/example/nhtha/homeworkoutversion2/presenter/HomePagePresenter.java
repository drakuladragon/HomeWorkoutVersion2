package com.example.nhtha.homeworkoutversion2.presenter;

import android.view.View;
import android.widget.ProgressBar;

import com.example.nhtha.homeworkoutversion2.callback.HomePageCallBackVIew;
import com.example.nhtha.homeworkoutversion2.callback.UserCallBack;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.model.Post;
import com.example.nhtha.homeworkoutversion2.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePagePresenter implements UserCallBack{

        private HomePageCallBackVIew homePageCallBackVIew;
        private DatabaseReference postReference;
        private List<Post> postList;
        private List<PostDto> postDtoList;
        private List<User> userList;
        private ProgressBar progressBar;
        private UserPresenter userPresenter;

        public HomePagePresenter(ProgressBar progressBar) {
            postReference = FirebaseDatabase.getInstance().getReference().child("post");
            userList = new ArrayList<>();
            postList = new ArrayList<>();
            postDtoList = new ArrayList<>();

            this.progressBar = progressBar;
            this.progressBar.setVisibility(View.VISIBLE);

            userPresenter = new UserPresenter();
            userPresenter.setCallBack(this);
        }

        public void loadPost(final String userName, final String userAvatarUrl){
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
                        post.setUserName(userName);
                        post.setUserAvatar(userAvatarUrl);

                        postList.add(post);
                        stopLoading();
                        homePageCallBackVIew.onLoadSucces(postList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    public void setHomePageCallBackVIew(HomePageCallBackVIew homePageCallBackVIew) {
        this.homePageCallBackVIew = homePageCallBackVIew;
    }

    private void stopLoading(){
            progressBar.setVisibility(View.GONE);
        }

    @Override
    public void onLoadSuccess(String userName, String userAvatarUrl) {
        loadPost(userName,userAvatarUrl);
    }
}
