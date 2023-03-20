/**
 * A model of a player in the QR Hunter game.
 */
package com.goblin.qrhunter;

import com.goblin.qrhunter.data.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * The Player class represents a user of the QRHunter application. It contains information
 * such as the user's ID, username, and contact information. Optionally, a profile picture
 * URL can also be included.
 *
 * The class implements the Entity interface, which provides a toMap() method for converting
 * the object to a map of key-value pairs (for use with Firebase).
 */
public class Player implements Entity, Serializable {
    private String id;
    private String username;
    private String contactInfo;
    private String phone;

    private String profileURI;


    private int totalScore;

    /**
     * Constructs an empty player object.
     * Required by Firebase.
     */
    public Player() {
        // require by firebase
    }

    /**
     * Constructs a new player object with the given ID, username, and contact information.
     * @param id The ID of the player.
     * @param username The username of the player.
     * @param contactInfo The contact information of the player.
     */
    public Player(String id, String username, String contactInfo) {
        setId(id);
        setUsername(username);
        setContactInfo(contactInfo);
    }



    /**
     * Constructs a new player object with the given ID, username, contact information, and profile picture URL.
     * @param id The ID of the player.
     * @param username The username of the player.
     * @param contactInfo The contact information of the player.
     * @param profilePicURL The profile picture URL of the player.
     * @param phone Phone number of the player.
     */
    public Player(String id, String username, String contactInfo, String profilePicURL, String phone) {
        setId(id);
        setUsername(username);
        setContactInfo(contactInfo);
        setPhone(phone);
        setProfileURI(profilePicURL);
    }

    /**
     * Returns a map representation of the player object for serialization.
     * @return A map representation of the player object.
     */
    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("username", getUsername());
        result.put("contactInfo", getContactInfo());
        result.put("phone", getPhone());
        return result;
    }


    /**
     * Returns the ID of the player.
     * @return The ID of the player.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the player.
     * @param id The ID to set for the player.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the username of the player.
     * @return username string.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player.
     * @param username The username to set for the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the contact information of the player.
     * @return contact information.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information (email) of the player.
     * @param contactInfo The contact information to set for the player.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Sets the phone number of player.
     * @param phone
     */
    public void setPhone(String phone) {this.phone = phone;}

    /**
     * Gets the phone number of player.
     * @return
     */
    public String getPhone() {return phone;}

    /**
     * Returns the profile picture URL of the player.
     * @return The profile picture URL of the player.
     */
    public String getProfileURI() {
        return profileURI;
    }

    /**
     * Sets the profile picture URL of the player.
     * @param profilePicURL The profile picture URL to set for the player.
     */
    public void setProfileURI(String profilePicURL) {
        this.profileURI = profilePicURL;
    }
    /**
     * Returns the total score of the player.
     * @return The total score of the player.
     */
    public int getTotalScore() {
        return totalScore;
    }
    /**
     * Adds points to the player's total score.
     * @param points The points to add to the player's total score.
     */
    public void addPoints(int points) {
        totalScore += points;
    }

    /**
     * Creates a new Player object that is a copy of the given Player object.
     *
     * @param original the Player object to be copied
     * @return a new Player object that has the same field values as the original object
     */
    static public Player copy(Player original) {
        Player newPlayer = new Player();
        newPlayer.setId(original.getId());
        newPlayer.setUsername(original.getUsername());
        newPlayer.setContactInfo(original.getContactInfo());
        newPlayer.setPhone(original.getPhone());
        newPlayer.setProfileURI(original.getProfileURI());
        return newPlayer;
    }

}
