package com.example.nhtha.homeworkoutversion2.dto;

/**
 * Created by nhtha on 06-Mar-18.
 */

public class PostDto {
    private String authorId;
    private String title;
    private String content;
    private String imageURL;
    private String date;

    public PostDto() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
