package com.example.nhtha.homeworkoutversion2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.model.Post;

import java.util.List;

/**
 * Created by nhtha on 07-Mar-18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> postList;
    private Context context;
    private OnPostItemClickListener onPostItemClickListener;
    private View view;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_post,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Post post = postList.get(position);
        holder.setTxtPostTitle(post.getTitle());
        holder.setTxtPostUsername(post.getUserName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostItemClickListener.onItemClick(position);
            }
        });

    }

    public void notifyDataChanged(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public Post getItem(int position){
        return postList.get(position);
    }

    @Override
    public int getItemCount() {
        if (postList != null) {
            return postList.size();
        }
        return 0;
    }

    public void setOnPostItemClickListener(OnPostItemClickListener onPostItemClickListener) {
        this.onPostItemClickListener = onPostItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPostTitle;
        private TextView txtPostUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPostTitle = itemView.findViewById(R.id.txt_post_title);
            txtPostUsername = itemView.findViewById(R.id.txt_post_author_id);
        }

//        public void setImgPostImage(Context context, String uri) {
//            Picasso.with(context).load(uri).into(imgPostImage);
//        }

        public void setTxtPostTitle(String postTitle) {
            txtPostTitle.setText(postTitle);
        }

        public void setTxtPostUsername(String postAuthor) {
            txtPostUsername.setText(postAuthor);
        }

    }

    public interface OnPostItemClickListener {

        void onItemClick(int position);

    }

}
