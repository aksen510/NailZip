package com.example.nailzip.model;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;        // 유튜브-publisher

    public Post(String postid, String postimage, String description, String shopname) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = shopname;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
