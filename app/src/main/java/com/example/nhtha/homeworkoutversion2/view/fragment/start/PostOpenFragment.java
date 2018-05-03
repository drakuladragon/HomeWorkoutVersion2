package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.adapter.CommentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nhtha on 08-Mar-18.
 */

@SuppressLint("ValidFragment")
public class PostOpenFragment extends Fragment implements View.OnClickListener {

    private String postID;
    private CircleImageView civAvatar;
    private TextView txtPostAuthorName;
    private TextView txtPostTitle;
    private TextView txtPostContent;
    private ImageView imgPostImage;
    private RelativeLayout rlEdit;

    private DatabaseReference databaseReference;
    private DatabaseReference commentReference;

    private RecyclerView rcvComment;
    private CommentAdapter commentAdapter;
    private List<CommentDto> commentDtoList;

    @SuppressLint("ValidFragment")
    public PostOpenFragment(String postID) {
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
        return inflater.inflate(R.layout.fragment_open_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(String.valueOf(postID));
        commentReference = FirebaseDatabase.getInstance().getReference().child("comment");
        civAvatar = getView().findViewById(R.id.civ_avatar);
        txtPostAuthorName = getView().findViewById(R.id.txt_post_author_name);
        txtPostTitle = getView().findViewById(R.id.txt_post_title);
        txtPostContent = getView().findViewById(R.id.txt_post_content);
        imgPostImage = getView().findViewById(R.id.img_post_image);
        rlEdit = getView().findViewById(R.id.rl_edit);

        rcvComment = getView().findViewById(R.id.rcv_comment_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvComment.setLayoutManager(layoutManager);

        pullData();

        commentAdapter = new CommentAdapter(getContext(), commentDtoList);

        rcvComment.setAdapter(commentAdapter);

        rlEdit.setOnClickListener(this);

        imgPostImage.setOnClickListener(this);
    }

    private void pullData() {
        if (postID.isEmpty()) {
            return;
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostDto postDto = dataSnapshot.getValue(PostDto.class);

                String authorName = postDto.getUserName();
                String postTitle = postDto.getTitle();
                String postContent = postDto.getContent();
                String avatarUri = postDto.getUserAvatar();

                txtPostAuthorName.setText(authorName);
                txtPostTitle.setText(postTitle);
                txtPostContent.setText(postContent);

                Picasso.with(getContext()).load(avatarUri).into(civAvatar);

                String postImageUri = "";
                postImageUri += postDto.getImageURL();
                Log.d("get post data", "onDataChange: " + postImageUri);

//                if (postImageUri == null) {
//                    Log.d("EQUALS", "onDataChange: ádfsadfasfsadfasdf");
//                    imgPostImage.setVisibility(View.GONE);
//                } else {
//                    Log.d("EQUALS", "onDataChange: " + postImageUri);
//                    Picasso.with(getContext()).load(postImageUri).into(imgPostImage);
//                }

                Picasso.with(getContext()).load(postImageUri).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imgPostImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        imgPostImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                //   Glide.with(getContext()).load(postImageUri).into(imgPostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                Log.d("DatabaseError", "onCancelled: " + databaseError);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit:
                ((StartActivity) getActivity()).showAddCommentFragment(postID);
                break;

            default:
                break;
        }
    }

    public void notifyDataChanged() {
        if (commentDtoList != null) {
            commentAdapter.notifiDataChanged(commentDtoList);
        }
    }
}
