package com.goblin.qrhunter;

import com.goblin.qrhunter.data.Entity;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Player implements Entity {
    private String id;
    private String username;
    private String contactInfo;
    
    private String profileURI;

    public Player() {
        // require by firebase
    }

    public Player(String id, String username, String contactInfo) {
        setId(id);
        setUsername(username);
        setContactInfo(contactInfo);
    }

    public Player(String id, String username, String contactInfo, String profilePicURL) {
        setId(id);
        setUsername(username);
        setContactInfo(contactInfo);
        setProfileURI(profilePicURL);
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("contactInfo", contactInfo);
        return result;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProfileURI() {
        return profileURI;
    }

    public void setProfileURI(String profilePicURL) {
        this.profileURI = profilePicURL;
    }


}
