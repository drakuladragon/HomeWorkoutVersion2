package com.example.nhtha.homeworkoutversion2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.CommentDto;
import com.example.nhtha.homeworkoutversion2.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nhtha on 09-Mar-18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.setCivAvatar(context, comment.getAuthorAvatar());
        holder.setTxtAuthorName(comment.getAuthorName());
        holder.setTxtCommentDes(comment.getCommentDes());
    }

    @Override
    public int getItemCount() {
        if (commentList != null) {
            return commentList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civAvatar;
        private TextView txtAuthorName;
        private TextView txtCommentDes;

        public ViewHolder(View itemView) {
            super(itemView);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            txtAuthorName = itemView.findViewById(R.id.txt_author_name);
            txtCommentDes = itemView.findViewById(R.id.txt_coment_description);
        }

        public void setCivAvatar(Context context, String avatarUri) {
            Picasso.with(context).load(avatarUri).into(civAvatar);
        }

        public void setTxtAuthorName(String name) {
            txtAuthorName.setText(name);
        }

        public void setTxtCommentDes(String commentDes) {
            txtCommentDes.setText(commentDes);
        }
    }

    public void notifiDataChanged(List<Comment> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    public void notifiDataChanged(Comment comment) {
        this.commentList.add(comment);
        notifyDataSetChanged();
    }



}
