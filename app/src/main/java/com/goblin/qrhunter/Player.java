package com.goblin.qrhunter;

import com.goblin.qrhunter.data.DAO;

import java.util.UUID;

public class Player implements DAO {
    private String uid;
    private String username;
    private String contactInfo;

    public String getId() {
        return uid;
    }

    public void setId(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Player(String username, String contactInfo) {
        this.username = username;
        this.contactInfo = contactInfo;
        this.uid = UUID.randomUUID().toString();
    }

    public Player(String uid, String username, String contactInfo) {
        this.uid = uid;
        this.username = username;
        this.contactInfo = contactInfo;
    }

    public Player() {
        // empty constructor for firebase
    }



}
