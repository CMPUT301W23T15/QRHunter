package com.goblin.qrhunter;

import com.goblin.qrhunter.data.Entity;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score implements Entity {
    private int totalScore;
    private List<QRCode> posts;

    private String playerId;
    private Player player;

    /**
     * Default constructor, initializes the total score to 0.
     */
    public Score() {
        totalScore = 0;
    }


    /**
     * Creates a new Score object with the provided total score and list of QRCode objects.
     *
     * @param totalScore The total score of the user's QR codes.
     * @param posts The list of QRCode objects associated with the user's score.
     */
    public Score(int totalScore, List<QRCode> posts) {
        this.totalScore = totalScore;
        this.posts = posts;
    }

    /**
     * Creates a new Score object with the provided total score, list of QRCode objects and player ID.
     *
     * @param totalScore The total score of the user's QR codes.
     * @param posts The list of QRCode objects associated with the user's score.
     * @param playerId The player ID associated with the user
     */
    public Score(int totalScore, List<QRCode> posts, String playerId){
        this.totalScore = totalScore;
        this.posts = posts;
        this.playerId = playerId;
    }

    /**
     * Returns the total score for all QR codes.
     *
     * @return The total score for all QR codes.
     */
    public int getTotalScore() {
        int sum = 0;
        if(this.posts == null) {
            return sum;
        }
        for (QRCode qr: this.posts
             ) {
            sum += qr.getScore();
        }
        return sum;
    }


    /**
     * Retrieves the list of QRCode objects associated with this score.
     *
     * @return A list of QRCode objects.
     */
    public List<QRCode> getPosts() {
        return posts;
    }

    /**
     * Sets the list of QRCode objects for this score and sorts them by score in descending order.
     *
     * @param posts A list of QRCode objects to set.
     */
    public void setPosts(List<QRCode> posts) {
        this.posts = posts;
        if (this.posts != null) {
            this.sortPosts();
        }
    }

    /**
     * Retrieves the Player object associated with this score.
     *
     * @return The associated Player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the Player object associated with this score.
     *
     * @param player The Player object to associate with this score.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Retrieves the player ID associated with this score.
     *
     * @return The player ID as a string.
     */
    public String getPlayerId() {
        return this.playerId;
    }

    /**
     * Sets the player ID associated with this score.
     *
     * @param playerId The player ID to set as a string.
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * Adds a QRCode object to the list of posts and sorts the list.
     *
     * @param qr The QRCode object to be added to the list of posts.
     */
    @Exclude
    public void addQRCode(QRCode qr) {
        if(this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(qr);
        this.sortPosts();
    }

    /**
     * Removes a QRCode object from the list of posts and sorts the list.
     *
     * @param qr The QRCode object to be removed from the list of posts.
     */
    @Exclude
    public void removeQRCode(QRCode qr) {
        if(this.posts != null) {
            this.posts.remove(qr);
            this.sortPosts();
        }
    }

    /**
     * Sorts the list of posts based on their score in descending order.
     */
    private void sortPosts() {
        if(this.posts != null) {
            this.posts.sort(Comparator.comparing(QRCode::getScore).reversed());
        }
    }

    /**
     * Retrieves the highest score among all QR codes.
     *
     * @return The highest score or 0 if no QR codes are available.
     */
    public int getHighestScore() {
        QRCode high = getHighestScoringQRCode();
        if(high == null) {
            return 0;
        }
        return high.getScore();
    }

    /**
     * Retrieves the lowest score among all QR codes.
     *
     * @return The lowest score or 0 if no QR codes are available.
     */
    public int getLowestScore() {
        QRCode low = getLowestScoringQRCode();
        if(low == null) {
            return 0;
        }
        return low.getScore();
    }

    /**
     * Retrieves the total number of QR codes associated with this score.
     *
     * @return The number of QR codes.
     */
    public int getQRCount() {
        if(this.posts == null) {
            return 0;
        }
        return this.posts.size();
    }

    /**
     * Retrieves the highest scoring QR code among all the associated QR codes.
     *
     * @return The highest scoring QR code or null if no QR codes are available.
     */
    @Exclude
    public QRCode getHighestScoringQRCode() {
        if (posts == null || posts.isEmpty()) {
            return null;
        }
        return posts.get(0);
    }

    /**
     * Retrieves the lowest scoring QR code among all the associated QR codes.
     *
     * @return The lowest scoring QR code or null if no QR codes are available.
     */
    @Exclude
    public QRCode getLowestScoringQRCode() {
        if (posts == null || posts.isEmpty()) {
            return null;
        }

        return posts.get(posts.size() - 1);
    }

    /**
     * Retrieves the player ID associated with this score.
     *
     * @return The player ID as a string.
     */
    @Override
    public String getId() {
        return playerId;
    }

    /**
     * Sets the player ID associated with this score.
     *
     * @param playerId The player ID to set as a string.
     */
    @Override
    public void setId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * Converts the current Score object into a map with key-value pairs representing its properties.
     *
     * @return A map containing the properties of the Score object.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>() {{
            put("totalScore", getTotalScore());
            put("lowestScore", getLowestScore());
            put("highestScore", getHighestScore());
            put("playerId", getPlayerId());
            put("qrcount", getQRCount());
            put("posts", getPosts());
        }};

        if(this.player != null) {
            map.put("player", this.player.toMap());
        }

        return map;
    }
}
