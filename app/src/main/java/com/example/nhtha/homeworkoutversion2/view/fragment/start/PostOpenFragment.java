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
import com.example.nhtha.homeworkoutversion2.callback.ImageLoaderCallBack;
import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.model.Comment;
import com.example.nhtha.homeworkoutversion2.model.Post;
import com.example.nhtha.homeworkoutversion2.presenter.ImageLoader;
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
public class PostOpenFragment extends Fragment implements View.OnClickListener,ImageLoaderCallBack {

    private Post post;
    private CircleImageView civAvatar;
    private TextView txtPostAuthorName;
    private TextView txtPostTitle;
    private TextView txtPostContent;
    private ImageView imgPostImage;
    private RelativeLayout rlEdit;

    private DatabaseReference commentReference;
    private ImageLoader imageLoader;

    private RecyclerView rcvComment;
    private CommentAdapter commentAdapter;
    private List<CommentDto> commentDtoList;

    @SuppressLint("ValidFragment")
    public PostOpenFragment(Post post) {
        this.post = post;
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

        commentReference = FirebaseDatabase.getInstance().getReference().child("comment");
        civAvatar = getView().findViewById(R.id.civ_avatar);
        txtPostAuthorName = getView().findViewById(R.id.txt_post_author_name);
        txtPostTitle = getView().findViewById(R.id.txt_post_title);
        txtPostContent = getView().findViewById(R.id.txt_post_content);
        imgPostImage = getView().findViewById(R.id.img_post_image);
        rlEdit = getView().findViewById(R.id.rl_edit);
        rcvComment = getView().findViewById(R.id.rcv_comment_list);

        imageLoader = new ImageLoader(getContext(),post.getImageURL(),this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvComment.setLayoutManager(layoutManager);

        pullData();

        commentAdapter = new CommentAdapter(getContext(), new ArrayList<Comment>());

        rcvComment.setAdapter(commentAdapter);

        rlEdit.setOnClickListener(this);

        imgPostImage.setOnClickListener(this);
    }

    private void pullData() {
        if (post == null) {
            return;
        }

        String authorName = post.getUserName();
        String postTitle = post.getTitle();
        String postContent = post.getContent();
        String avatarUri = post.getUserAvatar();

        txtPostAuthorName.setText(authorName);
        txtPostTitle.setText(postTitle);
        txtPostContent.setText(postContent);

        Picasso.with(getContext()).load(avatarUri).into(civAvatar);

        String postImageUri = "";
        postImageUri += post.getImageURL();
        Log.d("get post data", "onDataChange: " + postImageUri);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit:
                ((StartActivity) getActivity()).showAddCommentFragment(post.getPostID());
                break;

            default:
                break;
        }
    }

    public void notifyDataChanged() {
//        if (commentDtoList != null) {
//            commentAdapter.notifiDataChanged(commentDtoList);
//        }
    }

    @Override
    public void onLoadSucces(Bitmap bitmap) {
        imgPostImage.setVisibility(View.VISIBLE);
        imgPostImage.setImageBitmap(bitmap);
    }

    @Override
    public void onFail() {
        imgPostImage.setVisibility(View.GONE);
    }
}
