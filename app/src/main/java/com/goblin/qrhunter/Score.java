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

    public Score() {
        totalScore = 0;
    }

    public Score(int totalScore, List<QRCode> posts) {
        this.totalScore = totalScore;
        this.posts = posts;
    }

    /**
     * new Constructor for testing
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



    public List<QRCode> getPosts() {
        return posts;
    }

    public void setPosts(List<QRCode> posts) {
        this.posts = posts;
        if (this.posts != null) {
            this.sortPosts();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Exclude
    public void addQRCode(QRCode qr) {
        if(this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(qr);
        this.sortPosts();
    }

    @Exclude
    public void removeQRCode(QRCode qr) {
        if(this.posts != null) {
            this.posts.remove(qr);
            this.sortPosts();
        }
    }

    private void sortPosts() {
        if(this.posts != null) {
            this.posts.sort(Comparator.comparing(QRCode::getScore).reversed());
        }
    }


    public int getHighestScore() {
        QRCode high = getHighestScoringQRCode();
        if(high == null) {
            return 0;
        }
        return high.getScore();
    }

    public int getLowestScore() {
        QRCode low = getLowestScoringQRCode();
        if(low == null) {
            return 0;
        }
        return low.getScore();
    }


    public int getQRCount() {
        if(this.posts == null) {
            return 0;
        }
        return this.posts.size();
    }

    @Exclude
    public QRCode getHighestScoringQRCode() {
        if (posts == null || posts.isEmpty()) {
            return null;
        }
        return posts.get(0);
    }

    @Exclude
    public QRCode getLowestScoringQRCode() {
        if (posts == null || posts.isEmpty()) {
            return null;
        }

        return posts.get(posts.size() - 1);
    }


    @Override
    public String getId() {
        return playerId;
    }

    @Override
    public void setId(String playerId) {
        this.playerId = playerId;
    }

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
