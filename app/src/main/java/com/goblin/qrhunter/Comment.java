package com.goblin.qrhunter;


import java.util.UUID;

public class Comment {
    private String id;
    private String playerId;
    private String postId;
    private String text;

    public Comment() {

    }

    public Comment(String uid, String playerId, String postId, String text) {
        this.setPlayerId(playerId);
        this.setPostId(postId);
        this.setText(text);
        this.setId(uid);
    }

    public Comment(String playerId, String postId, String text) {
        this.setPlayerId(playerId);
        this.setPostId(postId);
        this.setText(text);
        this.setId(UUID.randomUUID().toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String uid) {
        this.id = uid;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
