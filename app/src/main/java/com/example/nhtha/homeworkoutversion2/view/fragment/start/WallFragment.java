package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.callback.PostCallBackVIew;
import com.example.nhtha.homeworkoutversion2.model.Post;
import com.example.nhtha.homeworkoutversion2.presenter.PostPresenter;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhtha on 26-Feb-18.
 */

public class WallFragment extends Fragment implements View.OnClickListener,PostAdapter.OnPostItemClickListener,PostCallBackVIew {

    private FloatingActionButton floatingActionButton;

    private PostAdapter postAdapter;
    private RecyclerView rcvPost;
    private List<String> postDtoID;

    private ProgressBar progressBar;

    private ImageView imgMenu;
    private PostPresenter postPresenter;

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
        floatingActionButton = getView().findViewById(R.id.fab_add_post);
        progressBar = getView().findViewById(R.id.pgb_loading);

        postPresenter = new PostPresenter(progressBar);
        postPresenter.setPostListView(this);
        postDtoID = new ArrayList<>();

        floatingActionButton.setOnClickListener(this);

        postAdapter = new PostAdapter(getContext(), new ArrayList<Post>());
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

        ((StartActivity) getActivity()).showPostOpenFragment(postAdapter.getItem(position));

    }

    @Override
    public void onLoadSucces(List<Post> postList) {
        postAdapter.notifyDataChanged(postList);
        progressBar.setVisibility(View.GONE);
    }
}
