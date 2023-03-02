package com.goblin.qrhunter;

import android.location.Location;


import java.util.UUID;

public class Post {
    private String id;
    private String name;
    private QRCode code;
    private String playerId;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public Post() {
        // empty constructor required by firebase
    }

    public Post(String name, QRCode code, String playerId) {
        this.name = name;
        this.code = code;
        this.playerId = playerId;
        this.id = UUID.randomUUID().toString();
    }

    public Post(String uid, String name, String comment, QRCode code, String playerId) {
        this.id = uid;
        this.name = name;
        this.code = code;
        this.playerId = playerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String uid) {
        this.id = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QRCode getCode() {
        return code;
    }

    public void setCode(QRCode code) {
        this.code = code;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
