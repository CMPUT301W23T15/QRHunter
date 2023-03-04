package com.goblin.qrhunter;

import android.location.Location;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.location.Location;

import com.goblin.qrhunter.data.Entity;

import java.util.UUID;

public class Post implements Entity {
    private String id;
    private String name;
    private QRCode code;
    private String playerId;
    private Location location;

    public Post() {
        // empty constructor required by Firebase
    }

    public Post(String name, QRCode code, String playerId) {
        setName(name);
        setCode(code);
        setPlayerId(playerId);
        setId(UUID.randomUUID().toString());
    }

    public Post(String id, String name, String comment, QRCode code, String playerId) {
        setId(id);
        setName(name);
        setCode(code);
        setPlayerId(playerId);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("code", code);
        map.put("playerId", playerId);
        map.put("location", location);
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}

