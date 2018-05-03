package com.example.nhtha.homeworkoutversion2.dto;

/**
 * Created by nhtha on 08-Mar-18.
 */

public class CommentDto {
    private String authorID;
    private String postID;
    private String authorName;
    private String authorAvatar;
    private String commentDes;

    public String getCommentDes() {
        return commentDes;
    }

    public void setCommentDes(String commentDes) {
        this.commentDes = commentDes;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    private String content;

    public CommentDto() {

    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
