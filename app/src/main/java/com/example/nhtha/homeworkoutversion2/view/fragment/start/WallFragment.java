package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.adapter.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhtha on 26-Feb-18.
 */

public class WallFragment extends Fragment implements View.OnClickListener,PostAdapter.OnPostItemClickListener {

    private TabHost tbhWall;
    private TabHost.TabSpec tabReview;
    private TabHost.TabSpec tabPost;

    private FloatingActionButton floatingActionButton;

    private PostAdapter postAdapter;
    private RecyclerView rcvPost;
    private List<PostDto> postDtoList;
    private List<String> postDtoID;

    private ProgressBar progressBar;

    private DatabaseReference postReference;
    private FirebaseDatabase firebaseDatabase;

    private ImageView imgMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void init() {
        postDtoList = new ArrayList<>();
        postDtoID = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        postReference = firebaseDatabase.getReference().child("post");

        floatingActionButton = getView().findViewById(R.id.fab_add_post);
        progressBar = getView().findViewById(R.id.pgb_loading);
//        tbhWall = getView().findViewById(R.id.tbh_wall);
//        tbhWall.setup();
//
//        tabReview = tbhWall.newTabSpec("tab1");
//        tabReview.setIndicator("Review");
//        tabReview.setContent(R.id.tab1);
//        tbhWall.addTab(tabReview);
//
//        tabPost = tbhWall.newTabSpec("tab2");
//        tabPost.setIndicator("Post");
//        tabPost.setContent(R.id.tab2);
//        tbhWall.addTab(tabPost);


        floatingActionButton.setOnClickListener(this);

        pullList();

        postAdapter = new PostAdapter(getContext(), postDtoList);
        postAdapter.setOnPostItemClickListener(this);

        rcvPost = getView().findViewById(R.id.rcv_post);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvPost.setLayoutManager(layoutManager);
        rcvPost.setAdapter(postAdapter);

        imgMenu = getView().findViewById(R.id.img_menu);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).openDrawer();
            }
        });

    }

    private void pullList() {
        progressBar.setVisibility(View.VISIBLE);
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postDtoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostDto postDto = snapshot.getValue(PostDto.class);
                    postDtoList.add(postDto);
                    postDtoID.add(snapshot.getKey());
                }

                postAdapter.notifyDataChanged(postDtoList);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ONCANCELD", "onCancelled: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_post:
                ((StartActivity) getActivity()).showPostFragment();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

        ((StartActivity) getActivity()).showPostOpenFragment(postDtoID.get(position));

    }
}
