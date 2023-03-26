/**
 * Model of a post made by a user.
 */
package com.goblin.qrhunter;

import android.location.Location;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.location.Location;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.goblin.qrhunter.data.Entity;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

/**
 * The `Post` class represents a post made by a player in the QR Hunter game. A post is associated with a QR code and a location.
 * It contains information about the player who made the post, the name of the post, and the location of the post.
 *
 * This class implements the `Entity` interface and provides a method to convert the object to a map, which is used to store the object in the database.
 *
 */
public class Post implements Entity, Serializable {
    private String id;
    private String name;
    private QRCode code;
    private String playerId;

    private double lat;
    private double lng;

    private String geohash;
    /**
     * Creates a new Post instance with default values.
     * This constructor is required by Firebase.
     */
    public Post() {
        // empty constructor required by Firebase
    }

    /**
     * Constructs a new Post object.
     *
     * @param name the name of the post
     * @param code the QR code associated with the post
     * @param playerId the ID of the player who created the post
     */
    public Post(String name, QRCode code, String playerId) {
        setName(name);
        setCode(code);
        setPlayerId(playerId);
        setId(UUID.randomUUID().toString());
    }

    /**
     * Creates a new Post instance with the specified ID, name, QRCode, and player ID.
     *
     * @param id        The unique identifier of the post.
     * @param name      The name of the post.
     * @param code      The QRCode associated with the post.
     * @param playerId  The unique identifier of the player who created the post.
     */
    public Post(String id, String name, String comment, QRCode code, String playerId) {
        setId(id);
        setName(name);
        setCode(code);
        setPlayerId(playerId);
    }

    /**
     * Returns a map representation of the Post object for serialization.
     * @return A map of the post
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", getName());
        map.put("code", getCode());
        map.put("playerId", getPlayerId());
        map.put("postKey", getPostKey());
        if(getGeoHash() != null) {
            map.put("lat", getLat());
            map.put("lng", getLng());
            map.put("geohash", getGeoHash());
        }
        return map;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
        getGeoHash();
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
        getGeoHash();
    }


    public String getGeoHash() {
        geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(getLat(), getLng()));
        return geohash;
    }

    /**
     * Returns the ID of the post.
     *
     * @return the ID of the post
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the post.
     *
     * @param id the ID of the post
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the post.
     *
     * @return the name of the post
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the post.
     *
     * @param name the name of the post
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the QR code associated with the post.
     *
     * @return the QR code associated with the post
     */
    public QRCode getCode() {
        return code;
    }

    /**
     * Sets the QR code associated with the post.
     *
     * @param code the QR code associated with the post
     */
    public void setCode(QRCode code) {
        this.code = code;
    }

    /**
     * Returns the ID of the player who created the post.
     *
     * @return the ID of the player who created the post
     */
    public String getPlayerId() {
        return playerId;
    }


    /**
     * Sets the ID of the player who created the post.
     *
     * @param playerId the ID of the player who created the post
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPostKey() {
        return playerId+code.getHash();
    }


}

