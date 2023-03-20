/**
 * Model of a comment that a user can make on a post
 */
package com.goblin.qrhunter;


import com.goblin.qrhunter.data.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Comment class represents a user's comment about a particular item or location in the QRHunter
 * application. It contains information such as the ID of the comment, the user's ID, the ID of
 * the item or location being commented on, and the text of the comment.
 *
 * The class implements the Entity interface, which provides a toMap() method for converting
 * the object to a map of key-value pairs (for use with Firebase).
 */
public class Comment implements Entity, Serializable {
    private String id;
    private String playerId;
    private String postId;
    private String text;

    /**
     * Empty constructor most used by firebase
     */
    public Comment() {

    }

    /**
     * create a new Comment with a id.
     * @param uid The unique identifier for the comment.
     * @param playerId The unique identifier for the player who made the comment.
     * @param postId The unique identifier for the post that the comment is associated with.
     * @param text The text of the comment.
     */
    public Comment(String uid, String playerId, String postId, String text) {
        this.setPlayerId(playerId);
        this.setPostId(postId);
        this.setText(text);
        this.setId(uid);
    }

    /**
     * create a new Comment and generate a new random uuid for the id.
     * @param playerId The unique identifier for the player who made the comment.
     * @param postId The unique identifier for the post that the comment is associated with.
     * @param text The text of the comment.
     */
    public Comment(String playerId, String postId, String text) {
        this.setPlayerId(playerId);
        this.setPostId(postId);
        this.setText(text);
        this.setId(UUID.randomUUID().toString());
    }

    /**
     * toMap serialization a comment into a Map
     * @return map of the serialized comment
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("playerId", playerId);
        map.put("postId", postId);
        map.put("text", text);
        return map;
    }


    /**
     * Returns the unique identifier for this comment.
     * @return The unique identifier for this comment.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this comment.
     * @param uid The unique identifier to set for this comment.
     */
    public void setId(String uid) {
        this.id = uid;
    }

    /**
     * Returns the unique identifier for the player who made this comment.
     * @return The unique identifier for the player who made this comment.
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Sets the unique identifier for the player who made this comment.
     * @param playerId The unique identifier to set for the player who made this comment.
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }


    /**
     * Returns the unique identifier for the post that this comment is on.
     * @return The unique identifier for the post that this comment is on.
     */
    public String getPostId() {
        return postId;
    }

    /**
     * Sets the unique identifier for the post that this comment is on.
     * @param postId The unique identifier to set for the post that this comment is on.
     */
    public void setPostId(String postId) {
        this.postId = postId;
    }

    /**
     * Returns the text of the comment.
     * @return The text of the comment.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the comment.
     * @param text The text to set for the comment.
     */
    public void setText(String text) {
        this.text = text;
    }
}
