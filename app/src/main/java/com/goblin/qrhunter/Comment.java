package com.goblin.qrhunter;


import com.goblin.qrhunter.data.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Comment implements Entity {
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

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("playerId", playerId);
        map.put("postId", postId);
        map.put("text", text);
        return map;
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
